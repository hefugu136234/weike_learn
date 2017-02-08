package com.lankr.tv_cloud.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @Description: Excel 导入导出
 */
public class ReadWriteExcelFile {

	/**
	 * @Description: 导入 Xls 文件	Excel 2003
	 */
	public static void readXLSFile() throws IOException {
		InputStream ExcelFileToRead = new FileInputStream("C:/Test.xls");
		HSSFWorkbook workbook = new HSSFWorkbook(ExcelFileToRead);
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row;
		HSSFCell cell;

		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			row = (HSSFRow) rows.next();
			
			Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				cell = (HSSFCell) cells.next();

				if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					System.out.print(cell.getStringCellValue() + " ");
				} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					System.out.print(cell.getNumericCellValue() + " ");
				} else {
					// U Can Handel Boolean, Formula, Errors
				}
			}
			System.out.println();
		}
	}

	
	/**
	 * @Description: 导出 Xls 文件	Excel 2003
	 */
	public static void writeXLSFile() throws IOException {
		String excelFileName = "C:/Test.xls";
		String sheetName = "Sheet1";
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);

		for (int rowNum = 0; rowNum < 5; rowNum++) {
			HSSFRow row = sheet.createRow(rowNum);

			// iterating c number of columns
			for (int cellNum = 0; cellNum < 5; cellNum++) {
				HSSFCell cell = row.createCell(cellNum);
				cell.setCellValue("Cell " + rowNum + " " + cellNum);
			}
		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		workbook.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

	
	/**
	 * @Description: 导入 Xlsx 文件	Excel 2007
	 */
	public static void readXLSXFile() throws IOException {
		InputStream ExcelFileToRead = new FileInputStream("C:/Test.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row;
		XSSFCell cell;

		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();
			
			Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				cell = (XSSFCell) cells.next();

				if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
					System.out.print(cell.getStringCellValue() + " ");
				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					System.out.print(cell.getNumericCellValue() + " ");
				} else {
					// U Can Handel Boolean, Formula, Errors
				}
			}
			System.out.println();
		}
	}

	/**
	 * @Description: 导出 Xlsx 文件	Excel 2007
	 */
	public static void writeXLSXFile() throws IOException {
		String excelFileName = "C:/Test.xlsx";
		String sheetName = "Sheet1";
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(sheetName);

		for (int rowNum = 0; rowNum < 5; rowNum++) {
			XSSFRow row = sheet.createRow(rowNum);

			for (int cellNum = 0; cellNum < 5; cellNum++) {
				XSSFCell cell = row.createCell(cellNum);
				cell.setCellValue("Cell " + rowNum + " " + cellNum);
			}
		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		workbook.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}
}
