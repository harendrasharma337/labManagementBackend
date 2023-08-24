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
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.labmanagement.bean.StudentExcelBean;
import com.labmanagement.exception.ExceptionOccur;
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

	public static List<StudentExcelBean> readXLSXAndMapToPOJO(MultipartFile file) throws Exception {
		File fileConverted = convertMultiPartToFile(file, null);
		List<StudentExcelBean> listOfStudentExcelBean = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(fileConverted); Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			if (rowIterator.hasNext()) {
                rowIterator.next();
            }
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				
				if (row.getCell(0) == null && row.getCell(1) == null
						&& row.getCell(3) == null) {
					break;
				}
				Cell fullName = row.getCell(0);
				Cell studentNumber = row.getCell(1);
				Cell email = row.getCell(2);
				Cell role = row.getCell(3);
				String fullName1 = fullName.getStringCellValue();
				Long studentNumber1 = (long) studentNumber.getNumericCellValue();
				String email1 = email.getStringCellValue();
				String role1 = role.getStringCellValue();
				if (fullName1 != null && String.valueOf(studentNumber1) != null && email1 != null && role1 != null) {
					StudentExcelBean studentExcelBean = new StudentExcelBean(fullName1, String.valueOf(studentNumber1), email1, role1);
					listOfStudentExcelBean.add(studentExcelBean);
                }else {
                	throw new ExceptionOccur(Messages.STUEDENT_DATA_MISSING.getValue());
                }
				
			}
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
