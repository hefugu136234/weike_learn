package com.lankr.tv_cloud.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 注：修改诊所字段于15-7-31 需要重新封装方法
 * 
 * @author Administrator
 *
 */
public class AnalyzeExcel {
	/**
	 * 解析诊所信息的excel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		AnalyzeExcel excel = new AnalyzeExcel();
		File file = new File("E:\\111.xls");
		List<ExcexlModel> cList = parseExcel(file);
		System.out.println(cList.size());
		String model=cList.get(0).getPrivince();
		System.out.println(model.substring(0, 3));
		System.out.println(model.substring(3, 6));
		for (ExcexlModel excexlModel : cList) {
			//System.out.println(excexlModel.privince+":"+excexlModel.city);
		}

	}

	public void getListByExcel(File flie) {
		// List<Clinic> list=new ArrayList<Clinic>();

	}


	/**
	 * Excel导入数据
	 */
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

	/**
	 * set 诊所属性 excel列属性 0=名称 1=省 2=市 3=地址 4=坐标 5=商圈 6=联系方式 7=营业时间
	 * 添加到list的规则=只有当名称、省、市属性齐全时添加，否则作废
	 */
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

	/**
	 * 修改字段于15-7-31，需要重新封装方法
	 * 
	 * 封装诊所的而每个属性
	 * 
	 * @param clinic
	 * @param cell
	 * @param columnId
	 * @return excel列属性 0=名称 1=省 2=市 3=地址 4=坐标 5=商圈 6=联系方式 7=营业时间
	 */
	// public static Clinic setModeClinic(Clinic clinic, String val, int
	// columnId) {
	// switch (columnId) {
	// case 0:
	// clinic.setName(val);
	// break;
	// case 1:
	// clinic.setProvince(val);
	// break;
	// case 2:
	// clinic.setCity(val);
	// break;
	// case 3:
	// clinic.setAddress(val);
	// break;
	// case 4:
	// if (!val.isEmpty()) {
	// String str[] = val.split(",");
	// if (str != null && str.length == 2) {
	// try {
	// float longitude = Float.parseFloat(str[0]);
	// float latitude = Float.parseFloat(str[1]);
	// clinic.setLongitude(longitude);
	// clinic.setLatitude(latitude);
	// } catch (NumberFormatException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	// break;
	// case 5:
	// clinic.setTradeArea(val);
	// break;
	// case 6:
	// clinic.setContactPhone(val);
	// break;
	// case 7:
	// clinic.setShopHours(val);
	// break;
	// default:
	// break;
	// }
	// return clinic;
	// }

	/**
	 * 处理联系电话
	 */
	// public static

	/**
	 * 得到每一个单元格的数据
	 * 
	 * @param cell
	 * @return
	 */
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

}


