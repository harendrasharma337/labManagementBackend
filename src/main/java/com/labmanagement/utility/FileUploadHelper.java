package com.labmanagement.utility;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.labmanagement.config.FileUploadConfig;
import com.labmanagement.domain.Labs;
import com.labmanagement.domain.Modules;
import com.labmanagement.domain.User;
import com.labmanagement.exception.ExceptionMessages;
import com.labmanagement.exception.FileUploadException;
import com.labmanagement.repository.LabsRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FileUploadHelper {

	private FileUploadConfig fileUploadConfig;

	private LabsRepository labsRepository;

	public Labs uploadFile(MultipartFile file, String expireDate, Integer totalMarks, Modules module, User user) {
		try {
			String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
			// Check if the file's name contains invalid characters
			if (originalFileName.contains("..")) {
				throw new FileUploadException(ExceptionMessages.INVALID_FILE + originalFileName);
			}
			Labs labUploaded = saveLabData(originalFileName, expireDate, totalMarks, module, user);
			if (!ObjectUtils.isEmpty(labUploaded)) {
				originalFileName = labUploaded.getId() + "_" + originalFileName;
				String filePath = fileUploadConfig.getUploadDir() + originalFileName;
				Files.createDirectories(Paths.get(fileUploadConfig.getUploadDir()));
				file.transferTo(new File(filePath));
			}
			return labUploaded;
		} catch (Exception e) {
			throw new FileUploadException(e.getMessage());
		}
	}

	private Labs saveLabData(String fileName, String expireDate, Integer totalMarks, Modules module, User user)
			throws ParseException {
		String pattern = "yyyy-MM-dd";
		Labs lab = new Labs();
		lab.setFileName(fileName);
		lab.setExpireDate(new SimpleDateFormat(pattern).parse(expireDate));
		lab.setModules(module);
		lab.setCreateDate(new Date());
		lab.setTotalLabsMarks(totalMarks);
		lab.setUploadedBy(user.getFullName());
		return labsRepository.save(lab);
	}
}
