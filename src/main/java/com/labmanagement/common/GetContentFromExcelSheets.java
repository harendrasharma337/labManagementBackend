package com.labmanagement.common;

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

import com.labmanagement.bean.StudentExcelBean;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class GetContentFromExcelSheets {

	public static List<StudentExcelBean> readCSVAndMapToPOJO(String filePath) throws Exception {
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            CsvToBean<StudentExcelBean> csvToBean = new CsvToBeanBuilder<StudentExcelBean>(csvReader)
                .withType(StudentExcelBean.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

            return csvToBean.parse();
        }
    }

	
	public static List<StudentExcelBean> readXLSXAndMapToPOJO(String filePath) throws Exception {
        List<StudentExcelBean> listOfStudentExcelBean = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
				Workbook workbook = new XSSFWorkbook(fis)) {
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
	
	
}
