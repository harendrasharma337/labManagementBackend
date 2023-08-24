package com.labmanagement.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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

import com.labmanagement.bean.StudentExcelBean;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetContentFromExcelSheets {

	public static List<StudentExcelBean> readCSVAndMapToPOJO(MultipartFile file) throws Exception {

		File fileConverted = convertMultiPartToFile(file, null);
		try (CSVReader csvReader = new CSVReader(new FileReader(fileConverted))) {
			CsvToBean<StudentExcelBean> csvToBean = new CsvToBeanBuilder<StudentExcelBean>(csvReader)
					.withType(StudentExcelBean.class).withIgnoreLeadingWhiteSpace(true).build();

			return csvToBean.parse();
		}
	}

	public static List<StudentExcelBean> readXLSXAndMapToPOJO(String filePath) throws Exception {
		List<StudentExcelBean> listOfStudentExcelBean = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Cell fullName = row.getCell(0);
				Cell studentNumber = row.getCell(1);
				Cell email = row.getCell(2);
				Cell role = row.getCell(3);
				String fullName1 = fullName.getStringCellValue();
				String studentNumber1 = studentNumber.getStringCellValue();
				String email1 = email.getStringCellValue();
				String role1 = role.getStringCellValue();
				StudentExcelBean studentExcelBean = new StudentExcelBean(fullName1, studentNumber1, email1, role1);
				listOfStudentExcelBean.add(studentExcelBean);
			}
			listOfStudentExcelBean.remove(0);
		}
		return listOfStudentExcelBean;
	}

	private static File convertMultiPartToFile(MultipartFile file, String fileName) throws IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

}
