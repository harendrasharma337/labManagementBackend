package com.labmanagement.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.labmanagement.bean.LabsBean;
import com.labmanagement.bean.ModulesBean;
import com.labmanagement.bean.StudentBean;
import com.labmanagement.bean.Students;
import com.labmanagement.bean.UserBean;
import com.labmanagement.bean.UserRegistration;
import com.labmanagement.common.BaseUrls;
import com.labmanagement.common.Constants;
import com.labmanagement.common.Messages;
import com.labmanagement.domain.Labs;
import com.labmanagement.domain.Marks;
import com.labmanagement.domain.ModuleRelation;
import com.labmanagement.domain.Modules;
import com.labmanagement.domain.Role;
import com.labmanagement.domain.RoleType;
import com.labmanagement.domain.User;
import com.labmanagement.domain.UserRole;
import com.labmanagement.exception.FileUploadException;
import com.labmanagement.exception.InValidDataException;
import com.labmanagement.exception.UserIsNotFoundException;
import com.labmanagement.repository.LabsRepository;
import com.labmanagement.repository.MarksRepository;
import com.labmanagement.repository.ModuleRelationRepository;
import com.labmanagement.repository.ModuleRepository;
import com.labmanagement.repository.RoleRepository;
import com.labmanagement.repository.UserRepository;
import com.labmanagement.response.APIResponse;
import com.labmanagement.response.ErrorResponse;
import com.labmanagement.utility.CommonUtility;
import com.labmanagement.utility.ExcelReadHelper;
import com.labmanagement.utility.FileUploadHelper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements IUserService {

	private UserRepository userRepository;

	private ModelMapper modelMapper;

	private RoleRepository roleRepository;

	private CommonUtility commonUtility;

	private ModuleRepository moduleRepository;

	private MarksRepository marksRepository;

	private LabsRepository labsRepository;

	private FileUploadHelper fileUploadHelper;

	private ModuleRelationRepository moduleRelationRepository;

	private Environment environment;

	@Override
	public APIResponse<Object> createUser(UserRegistration userRegistration, HttpServletRequest request) {
		log.info("Inside UserService user registeration start...");
		List<ErrorResponse> errorResponse = validateUser(userRegistration);
		if (errorResponse.isEmpty()) {
			saveUser(userRegistration);
			log.info("Inside UserService user registered sucessfully...");
			return APIResponse.<Object>builder().status(Constants.SUCCESS.getValue())
					.message(Messages.USER_REGISTER_SUCCESSFULLY.getValue()).build();
		}
		return APIResponse.<Object>builder().status(String.valueOf(Constants.FAILED.getValue())).errors(errorResponse)
				.message(Messages.USER_VALIDATION_FAILED.getValue()).build();
	}

	@Override
	public APIResponse<String> uploadStudents(Long moduleId, MultipartFile file) {
		try {
			List<StudentBean> students = excelFileReader(file);
			Optional<Modules> fetchModulesDb = moduleRepository.findById(moduleId);
			List<ModuleRelation> moduleRelationDb;
			if (!fetchModulesDb.isPresent())
				throw new UserIsNotFoundException(Messages.MODULE_NOT_FOUND.getValue());
			moduleRelationDb = moduleRelationRepository.findAllByModules(fetchModulesDb.get());
			List<User> listOfUser = moduleRelationDb.stream().map(ModuleRelation::getUser).collect(Collectors.toList());
			students.removeIf(
					student -> listOfUser.stream().anyMatch(user -> user.getUsername().equals(student.getEmail())));
			students.forEach(studentObj -> {
				UserRegistration userBean = mapIntoUserRegestration(studentObj);
				List<ErrorResponse> errorResponse = validateUser(userBean);
				if (!errorResponse.isEmpty())
					throw new InValidDataException(errorResponse.get(0).getErrorMessage());
				User user = saveUser(userBean);
				moduleRepository.findById(moduleId).ifPresent(modulesDb -> {
					ModuleRelation moduleRelation = new ModuleRelation();
					moduleRelation.setModules(modulesDb);
					moduleRelation.setUser(user);
					moduleRelationRepository.save(moduleRelation);
				});
			});
		} catch (Exception ex) {
			throw new FileUploadException(ex.getMessage());
		}
		return APIResponse.<String>builder().results(Messages.USER_REGISTER_SUCCESSFULLY.getValue())
				.status(Constants.SUCCESS.getValue()).message(Messages.DATA_FETCHED_SUCCESSFULLY.getValue()).build();
	}

	private List<StudentBean> excelFileReader(MultipartFile file) {
		return Optional.ofNullable(file).filter(f -> !f.isEmpty()).map(f -> file.getOriginalFilename().toLowerCase())
				.map(filename -> {
					if (filename.endsWith(".csv")) {
						return ExcelReadHelper.readCSVAndMapToPOJO(file);
					} else if (filename.endsWith(".xlsx")) {
						return ExcelReadHelper.readXLSXAndMapToPOJO(file);
					} else {
						throw new FileUploadException(Messages.IN_VALID_FILE.getValue());
					}
				}).orElseThrow(() -> new FileUploadException(Messages.FILE_MISSING.getValue()));
	}

	private UserRegistration mapIntoUserRegestration(StudentBean studentObj) {
		UserRegistration userBean = new UserRegistration();
		userBean.setFullName(studentObj.getFullName());
		userBean.setUsername(studentObj.getEmail());
		userBean.setStudentNumber(studentObj.getStudentNumber());
		userBean.setRole(studentObj.getRole());
		userBean.setPassword("123456");
		userBean.setConfirmPassword("123456");
		return userBean;
	}

	private User saveUser(UserRegistration userRegistration) {
		log.info("Inside UserService saving user...");
		var user = modalMapper(userRegistration);
		Role role = new Role();
		setRoles(userRegistration, role);
		Set<UserRole> userRole = new HashSet<>();
		userRole.add(new UserRole(user, role));
		userRole.forEach(roles -> roleRepository.save(roles.getRole()));
		user.getUserRoles().addAll(userRole);
		return userRepository.save(user);
	}

	private void setRoles(UserRegistration userRegistration, Role role) {
		if (userRegistration.getRole().equals("ADMIN")) {
			role.setRoleId(1L);
			role.setName(RoleType.ADMIN.toString());
		} else if (userRegistration.getRole().equals("STUDENT")) {
			role.setRoleId(2L);
			role.setName(RoleType.STUDENT.toString());
		} else if (userRegistration.getRole().equals("PROFESSOR")) {
			role.setRoleId(3L);
			role.setName(RoleType.PROFESSOR.toString());
		} else if (userRegistration.getRole().equals("LAB_ASSISTANT")) {
			role.setRoleId(4L);
			role.setName(RoleType.LAB_ASSISTANT.toString());
		}
	}

	private User modalMapper(UserRegistration userRegistration) {
		var user = modelMapper.map(userRegistration, User.class);
		user.setCreateDate(new Date());
		user.setIsDeleted("N");
		return user;
	}

	private List<ErrorResponse> validateUser(UserRegistration userRegistration) {
		log.info("Inside UserService validating user...");
		List<ErrorResponse> list = new ArrayList<>();
		commonUtility.validateName(userRegistration, list);
		commonUtility.validatePassword(userRegistration, list);
		commonUtility.validateEmail(userRegistration, list);
		return list;
	}

	@Override
	public APIResponse<List<ModulesBean>> fetchModules(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			List<ModulesBean> modules = user.get().getModules().stream()
					.map(module -> modelMapper.map(module.getModules(), ModulesBean.class))
					.collect(Collectors.toList());
			return APIResponse.<List<ModulesBean>>builder().results(modules).status(Constants.SUCCESS.getValue())
					.message(Messages.DATA_FETCHED_SUCCESSFULLY.getValue()).build();
		}
		return APIResponse.<List<ModulesBean>>builder().results(new ArrayList<>()).status(Constants.SUCCESS.getValue())
				.message(Messages.DATA_NOT_FOUND.getValue()).build();
	}

	@Override
	public APIResponse<List<UserBean>> fetchLabAssistentsModulesBy(Long id) {
		Optional<Modules> module = moduleRepository.findById(id);
		if (module.isPresent()) {
			List<UserBean> labAssistents = module.get().getModulesRelation().stream().map(ModuleRelation::getUser)
					.collect(Collectors.toList()).stream()
					.filter(mr -> mr.getAuthorities().stream().anyMatch(
							(authority -> authority.getAuthority().equals(RoleType.LAB_ASSISTANT.toString()))))
					.map(user -> modelMapper.map(user, UserBean.class)).collect(Collectors.toList());
			return APIResponse.<List<UserBean>>builder().results(labAssistents).status(Constants.SUCCESS.getValue())
					.message(Messages.DATA_FETCHED_SUCCESSFULLY.getValue()).build();
		}
		return APIResponse.<List<UserBean>>builder().results(new ArrayList<>()).status(Constants.SUCCESS.getValue())
				.message(Messages.DATA_NOT_FOUND.getValue()).build();
	}

	@Override
	public APIResponse<List<Object>> fetchStudentsModulesBy(Long moduleId) {
		return moduleRepository.findById(moduleId).map(module -> {
			List<Object> dataSet = new ArrayList<>();
			List<Students> students = module.getModulesRelation().stream().map(ModuleRelation::getUser)
					.filter(mr -> mr.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get()
							.equals(RoleType.STUDENT.toString()))
					.collect(Collectors.toList()).stream().map(std -> {
						Students student = modelMapper.map(std, Students.class);
						List<LabsBean> labs = marksRepository.findByUser(std).stream().map(mark -> {
							LabsBean lab = new LabsBean();
							student.setTotalMarks(student.getTotalMarks() + mark.getTotalMarks());
							student.setTotalLabMarks(student.getTotalLabMarks() + mark.getLabs().getTotalLabsMarks());
							lab.setFileName(mark.getLabs().getFileName());
							lab.setId(mark.getLabs().getId());
							lab.setTotalLabsMarks(mark.getLabs().getTotalLabsMarks());
							lab.setEarnedMarks(mark.getTotalMarks());
							return lab;
						}).collect(Collectors.toList());
						student.setLabs(labs);
						return student;
					}).sorted(Comparator.comparing(Students::getName)) // Sort by student name
					.collect(Collectors.toList());

			List<LabsBean> moduleLabs = labsRepository.findByModules(module).stream()
					.map(lab -> modelMapper.map(lab, LabsBean.class)).collect(Collectors.toList());

			HashMap<String, Object> pairedData = new HashMap<>();
			pairedData.put("totalLabs", moduleLabs);
			pairedData.put("student_records", students);
			dataSet.add(pairedData);

			return APIResponse.<List<Object>>builder().results(dataSet).status(Constants.SUCCESS.getValue())
					.message(students.isEmpty() ? Messages.DATA_NOT_FOUND.getValue()
							: Messages.DATA_FETCHED_SUCCESSFULLY.getValue())
					.build();
		}).orElse(APIResponse.<List<Object>>builder().results(new ArrayList<>()).status(Constants.SUCCESS.getValue())
				.message(Messages.DATA_NOT_FOUND.getValue()).build());
	}

	@Override
	public APIResponse<List<LabsBean>> fetchLabsModulesBy(Long moduleId) {
		return moduleRepository.findById(moduleId).map(module -> {
			List<LabsBean> labs = labsRepository.findByModules(module).stream()
					.map(lab -> modelMapper.map(lab, LabsBean.class)).collect(Collectors.toList());
			return APIResponse.<List<LabsBean>>builder().results(labs).status(Constants.SUCCESS.getValue()).message(
					labs.isEmpty() ? Messages.DATA_NOT_FOUND.getValue() : Messages.DATA_FETCHED_SUCCESSFULLY.getValue())
					.build();
		}).orElse(APIResponse.<List<LabsBean>>builder().results(new ArrayList<>()).status(Constants.SUCCESS.getValue())
				.message(Messages.DATA_NOT_FOUND.getValue()).build());
	}

	@Override
	public APIResponse<List<Students>> fetchStudentsByLabs(Long labId) {
		return labsRepository.findById(labId).map(lab -> {
			List<Students> students = marksRepository.findByLabs(lab).stream()
					.map(mark -> userRepository.findById(mark.getUser().getId()).map(user -> {
						Students student = modelMapper.map(user, Students.class);
						student.setAnswerSheet(mark.getAnswerSheet());
						student.setMarksId(mark.getId());
						student.setTotalMarks(mark.getTotalMarks());
						return student;
					}).orElse(null)).collect(Collectors.toList());
			return APIResponse.<List<Students>>builder().results(students).status(Constants.SUCCESS.getValue())
					.message(students.isEmpty() ? Messages.DATA_NOT_FOUND.getValue()
							: Messages.DATA_FETCHED_SUCCESSFULLY.getValue())
					.build();
		}).orElse(APIResponse.<List<Students>>builder().results(new ArrayList<>()).status(Constants.SUCCESS.getValue())
				.message(Messages.DATA_NOT_FOUND.getValue()).build());

	}

	@Override
	@Transactional
	public APIResponse<String> uploadLab(MultipartFile file, String expireDate, Integer totalMarks, Long moduleId,
			User user) {
		log.info("UserService :: Upload Lab start...!");
		return moduleRepository.findById(moduleId).map(module -> {
			Labs isUplaoded = fileUploadHelper.uploadFile(file, expireDate, totalMarks, module, user);
			if (!ObjectUtils.isEmpty(isUplaoded)) {
				log.info("UserService :: Upload Lab end...!");
				return APIResponse.<String>builder().status(Constants.SUCCESS.getValue())
						.message(Messages.LAB_UPLOADED.getValue()).build();
			}
			return APIResponse.<String>builder().message(Messages.LAB_NOT_UPLOADED.getValue())
					.status(Constants.FAILED.getValue()).build();
		}).orElse(APIResponse.<String>builder().status(Constants.FAILED.getValue())
				.message(Messages.MODULE_NOT_FOUND.getValue()).build());
	}

	@Override
	public APIResponse<String> updateLab(LabsBean labReq, MultipartFile file) {
		Optional<Labs> lab = labsRepository.findById(labReq.getId());
		if (lab.isPresent()) {
			fileUploadHelper.updateLab(labReq, file, lab.get());
			return APIResponse.<String>builder().status(Constants.SUCCESS.getValue())
					.message(Messages.LAB_UPDATED.getValue()).build();
		}
		return APIResponse.<String>builder().status(Constants.FAILED.getValue())
				.message(Messages.LAB_NOT_UPDATED.getValue()).build();
	}

	@Override
	public APIResponse<String> updateStudentMarks(Students students, Long labId) {
		return userRepository.findById(students.getId()).map(u -> {
			Optional<Marks> sMark = marksRepository.findByUser(u).stream()
					.filter(mark -> mark.getLabs().getId().equals(labId)).findFirst();
			if (sMark.isPresent()) {
				sMark.get().setTotalMarks(students.getTotalMarks());
				marksRepository.save(sMark.get());
			}
			return APIResponse.<String>builder().status(Constants.SUCCESS.getValue())
					.message(Messages.STUDENT_MARKS_UPDATED.getValue()).results(null).build();
		}).orElse(APIResponse.<String>builder().status(Constants.FAILED.getValue())
				.message(Messages.STUDENT_NOT_FOUND.getValue()).build());
	}

	@Override
	public APIResponse<String> uploadStudentReview(Long studentId, MultipartFile uploadfile, Long labId) {
		if (uploadfile == null || uploadfile.isEmpty())
			throw new FileUploadException(Messages.FILE_MISSING.getValue());
		if (isSupportedFileExtension(uploadfile))
			return uploadFile(uploadfile, studentId, labId);
		else
			throw new FileUploadException(Messages.IN_VALID_FILE.getValue());
	}

	private boolean isSupportedFileExtension(MultipartFile uploadfile) {
		String fileNameExt = uploadfile.getOriginalFilename();
		if (fileNameExt != null) {
			fileNameExt = fileNameExt.toLowerCase();
			return fileNameExt.endsWith(".csv") || fileNameExt.endsWith(".xlsx") || fileNameExt.endsWith(".pdf");
		}
		return false;
	}

	private APIResponse<String> uploadFile(MultipartFile uploadfile, Long studentId, Long labId) {
		userRepository.findById(studentId)
				.ifPresentOrElse(user -> marksRepository.findByUser(user).stream()
						.filter(mark -> Objects.equals(mark.getLabs().getId(), labId)).findFirst()
						.ifPresentOrElse(mark -> processingStudentFeedback(uploadfile, studentId, mark), () -> {
							throw new FileUploadException(Messages.INVALID_LAB_ID.getValue());
						}), () -> {
							throw new UserIsNotFoundException(Messages.USER_DOES_NOT_EXIST.getValue());
						});
		return APIResponse.<String>builder().status(Constants.SUCCESS.getValue())
				.message(Messages.FILE_UPLOADED.getValue()).build();
	}

	private void processingStudentFeedback(MultipartFile uploadfile, Long studentId, Marks mark) {
		try {
			mark.setFeedback(studentId + "_" + uploadfile.getOriginalFilename());
			marksRepository.save(mark);
			String uploadDirectoryPath = environment.getProperty(BaseUrls.UPLOAD_REVIEW_DIRECTORY);
			File uploadDir = new File(uploadDirectoryPath);
			if (!uploadDir.exists())
				uploadDir.mkdirs();
			File destFile = new File(uploadDir, uploadfile.getOriginalFilename());
			uploadfile.transferTo(destFile);
		} catch (IllegalStateException | IOException e) {
			throw new FileUploadException(Messages.FILE_NOT_UPLOADED.getValue());
		}
	}

	@Override
	public APIResponse<String> uploadAnswerSheet(Long studentId, MultipartFile uploadfile, Long labId) {
		if (uploadfile == null || uploadfile.isEmpty())
			throw new FileUploadException(Messages.FILE_MISSING.getValue());
		if (isSupportedFileExtension(uploadfile))
			return uploadAnswerSheet(uploadfile, studentId, labId);
		else
			throw new FileUploadException(Messages.IN_VALID_FILE.getValue());
	}

	private APIResponse<String> uploadAnswerSheet(MultipartFile uploadfile, Long studentId, Long labId) {
		userRepository.findById(studentId)
				.ifPresentOrElse(user -> labsRepository.findById(labId).ifPresentOrElse(lab -> {
					Marks marks = new Marks();
					marks.setUser(user);
					marks.setLabs(lab);
					marks.setModules(lab.getModules());
					marks.setAnswerSheet(studentId + "_" + uploadfile.getOriginalFilename());
					marksRepository.save(marks);
					uploadAnswerSheetFile(uploadfile);
				}, () -> {
					throw new FileUploadException(Messages.INVALID_LAB_ID.getValue());
				}), () -> {
					throw new UserIsNotFoundException(Messages.USER_DOES_NOT_EXIST.getValue());
				});
		return APIResponse.<String>builder().status(Constants.SUCCESS.getValue())
				.message(Messages.FILE_UPLOADED.getValue()).build();
	}

	private void uploadAnswerSheetFile(MultipartFile uploadfile) {
		try {
			String uploadDirectoryPath = environment.getProperty(BaseUrls.UPLOAD_ANSWER_SHEET_DIRECTORY);
			File uploadDir = new File(uploadDirectoryPath);
			if (!uploadDir.exists())
				uploadDir.mkdirs();
			File destFile = new File(uploadDir, uploadfile.getOriginalFilename());
			uploadfile.transferTo(destFile);
		} catch (IllegalStateException | IOException e) {
			throw new FileUploadException(Messages.FILE_NOT_UPLOADED.getValue());
		}
	}
}
