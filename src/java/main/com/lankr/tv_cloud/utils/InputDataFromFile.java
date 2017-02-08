package com.lankr.tv_cloud.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.utils.JDBCOperationByPreparedStatement;
import com.lankr.tv_cloud.utils.Tools;

/**
 * @Description: 数据导入
 * @date 2016年7月15日
 */
public class InputDataFromFile {

	public static void main(String[] args){
		// 单列导入(文本文件)
		//singleColmn();
		
		// 多列导入(excel 2003)
		//mutilColmXls();
		
		// 多列导入(excel 2007)
		//mutilColmXlsx();
	}

	private static void singleColmn() {
		int provinceId = 1;
		int cityId = 229;
		
		File file = new File("/Users/mayuan/Desktop/20160715-addhospital.txt");
		BufferedReader reader = null;
		try {
			System.out.println("start...");
			System.out.println();
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			while ((tempString = reader.readLine()) != null) {
				System.out.println("读取到数据：line " + line + ": " + tempString);
				
				Province province = new Province();
				province.setId(provinceId);
				
				City city = new City();
				city.setId(cityId);
				
				Hospital hospital = new Hospital();
				hospital.setUuid(Tools.getUUID());
				hospital.setName(tempString);
				hospital.setGrade("一级医院");
				hospital.setMobile("");
				hospital.setAddress("");
				hospital.setProvince(province);
				hospital.setCity(city);
				hospital.setIsActive(1);
				try {
					JDBCOperationByPreparedStatement.insertRecordIntoTable(hospital);
					System.out.println("=====success===== LineNum:" + line);
					System.out.println();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("插入数据失败 LineNum:" + line);
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}	
			}
		}
	}
	
	private static void mutilColmXls() {
		File file = new File("/Users/mayuan/Desktop/new_hospital_160715.xlsx");
		List<ExcexlModel> cList = parseExcel(file);
		for(ExcexlModel model : cList){
			System.out.println(model.toString());
		}
	}
	
	public static List<ExcexlModel> parseExcel(File file) {
		List<ExcexlModel> list = new ArrayList<ExcexlModel>();
		try {
			// 打开Excel文件
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(
					new FileInputStream(file));
			// 打开Excel工作簿
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
			// 获取第一个sheet
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 获取第0行，即标题行
			// HSSFRow hssfRow = hssfSheet.getRow(0);
			// 获取标题行的总列数
			// int columnNum = hssfRow.getPhysicalNumberOfCells();
			// 获取sheet的总行数
			int rowNum = hssfSheet.getLastRowNum();

			// 所有数据当为String类型
			// 循环每一行
			HSSFRow rowAuto = null;
			for (int i = 1; i <= rowNum; i++) {
				rowAuto = hssfSheet.getRow(i);
				buildData(rowAuto, list);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	public static void buildData(HSSFRow hssfRow, List<ExcexlModel> list) {
		// vaildFlag 每个对象是否有效标志 vaildFlag=3才有效
		HSSFCell cell = null;
		ExcexlModel model = new ExcexlModel();
		for (int i = 0; i <= hssfRow.getPhysicalNumberOfCells(); i++) {
			cell = hssfRow.getCell(i);
				if (i == 0) {
					String value = getCellValue(cell);
					model.privince = value.trim();
				} else if (i == 1) {
					String value = getCellValue(cell);
					model.city = value.trim();
				} if(i>2){
					continue;
				}
		}
		boolean flag = !model.privince.isEmpty() && !model.city.isEmpty();
		if (flag)
			list.add(model);

	}
	
	public static String getCellValue(HSSFCell cell) {
		String valueString = "";
		if (cell != null) {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
				// 返回布尔类型的值
				valueString = String.valueOf(cell.getBooleanCellValue()).trim();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				// 返回数值类型的值
				DecimalFormat df = new DecimalFormat("0");  
				valueString = df.format(cell.getNumericCellValue()).trim();
			} else {
				// 返回字符串类型的值
				valueString = String.valueOf(cell.getStringCellValue()).trim();
			}
		}
		return valueString;
	}
	
	private static void mutilColmXlsx() {
		try {
			InputStream ExcelFileToRead = new FileInputStream("/Users/mayuan/Desktop/new_hospital_160715.xlsx");
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
