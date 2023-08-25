package com.labmanagement.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.labmanagement.bean.StudentBean;
import com.labmanagement.common.Messages;
import com.labmanagement.exception.FileUploadException;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelReadHelper {

	public static List<StudentBean> readCSVAndMapToPOJO(MultipartFile file) {
		try {
			CSVReader csvReader = new CSVReader(new FileReader(convertMultiPartToFile(file)));
			CsvToBean<StudentBean> csvToBean = new CsvToBeanBuilder<StudentBean>(csvReader).withType(StudentBean.class)
					.withIgnoreLeadingWhiteSpace(true).build();
			return csvToBean.parse();
		} catch (Exception e) {
			log.error("Error while mapping excel data in bean : {}", e.getMessage());
		}
		return new ArrayList<>();
	}

	public static List<StudentBean> readXLSXAndMapToPOJO(MultipartFile multiPartFile) {
		try (FileInputStream fis = new FileInputStream(convertMultiPartToFile(multiPartFile));
				Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheetAt(0);
			return processCell(sheet.iterator());
		} catch (Exception e) {
			log.error("Error while read excel : {}", e.getLocalizedMessage());
		}
		return new ArrayList<>();
	}

	private static List<StudentBean> processCell(Iterator<Row> rowIterator) {
		List<StudentBean> listOfStudentExcelBean = new ArrayList<>();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Cell fullName = row.getCell(0);
			Cell studentNumber = row.getCell(1);
			Cell email = row.getCell(2);
			Cell role = row.getCell(3);
			validateExcelData(fullName, studentNumber, email, role);
			String fullNameValue = fullName.getStringCellValue();
			long studentNumberValue = (long) studentNumber.getNumericCellValue();
			String emailValue = email.getStringCellValue();
			String roleValue = role.getStringCellValue();
			StudentBean studentExcelBean = new StudentBean(fullNameValue, studentNumberValue, emailValue, roleValue);
			listOfStudentExcelBean.add(studentExcelBean);
		}
		return listOfStudentExcelBean;
	}

	private static void validateExcelData(Cell fullName, Cell studentNumber, Cell email, Cell role) {
		if (fullName == null || studentNumber == null || email == null || role == null) {
			throw new FileUploadException(Messages.STUEDENT_DATA_MISSING.getValue());
		}
	}

	private static File convertMultiPartToFile(MultipartFile file) {
		try {
			File convFile = new File(file.getOriginalFilename());
			file.transferTo(convFile);
			return convFile;
		} catch (Exception e) {
			log.error("Error while file converted : {}", e.getLocalizedMessage());
		}
		return null;

	}

}
