package com.monitor.argus.common.util.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 导出excel报表
 * 
 * @Author null
 * @Date 2014-3-13 上午11:18:26
 * 
 */
public class ExportExcel {

	private String fileUrl;

	/**
	 * 创建实例，并初始化模板
	 * 
	 * @param fileUrl
	 *            模板路径
	 */
	public ExportExcel(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * 
	 * @param rows
	 *            模板内配置所占行数
	 * @param cols
	 *            模板内配置所占列数
	 * @param result
	 *            要导出报表的数据集
	 * @return excel报表
	 * 
	 */
	public InputStream export(int rows, int cols, List<?> result, Class<?> cls) {
		try {

			// POIFSFileSystem fs = new
			// POIFSFileSystem(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileUrl));
			POIFSFileSystem fis = new POIFSFileSystem(new FileInputStream(
					fileUrl));
			HSSFWorkbook wb = new HSSFWorkbook(fis);

			HSSFDataFormat format = wb.createDataFormat();
			HSSFCellStyle style;
			HSSFSheet sheet = wb.getSheetAt(0);
			GetProperty gp = new GetProperty();
			HSSFRow namesRow = sheet.getRow(rows - 2);
			HSSFRow typeRow = sheet.getRow(rows - 1);
			style = wb.createCellStyle();
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
			style.setWrapText(true);// 指定单元格自动换行

			HSSFFont font = wb.createFont();
			// 设置字体大小;
			font.setFontHeightInPoints((short) 10);
			style.setFont(font);
			for (int i = 0; i < result.size(); i++) {
				HSSFRow row = sheet.getRow(rows + i);
				if (row == null) {
					row = sheet.createRow(rows + i);
				}
				Object ov = (result.get(i));

				for (int j = 0; j < cols; j++) {
					HSSFCell cell = row.getCell((short) j);
					if (cell == null) {
						cell = row.createCell((short) j);
					}
					// 获得方法名
					HSSFCell nameCell = namesRow.getCell((short) j);
					HSSFCell typeCell = typeRow.getCell((short) j);
					String name = nameCell.getStringCellValue();
					String type = typeCell.getStringCellValue();
					// java反射机制获得方法值
					Object obj = gp.getProperty(name, cls, ov);
					// 写入Excel中
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					if ("Number".toLowerCase().equals(type)) {
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						if (obj != null && !"".equals(obj)) {
							cell.setCellValue(Double.valueOf(String
									.valueOf(obj)));
							// 设定样式
							style.setDataFormat(format.getFormat("#.##")); // 指定number类型显示格式
							cell.setCellStyle(style);
						} else {
							cell.setCellValue("");
						}
					} else if ("Date".toLowerCase().equals(type)) {
						sheet.setColumnWidth((short) j, (short) (64 * 80));// 设置某一列的列宽
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						if (obj != null && !"".equals(obj)) {
							style.setDataFormat(format
									.getFormat("yyyy-MM-dd HH:mm:ss"));
							style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
							cell.setCellStyle(style);
							// cell.setCellValue(sdf.parse(String.valueOf(obj)));
							DateFormat df = DateFormat.getDateInstance();
							cell.setCellValue(sdf.format(obj));
						} else {
							cell.setCellValue("");
						}
					} else {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (obj == null) {
							cell.setCellValue("");
						} else {
							// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
							// 指定单元格居中对齐
							String str = String.valueOf(obj);
							if (str.length() > 15) {
								sheet.setColumnWidth((short) j,
										(short) (64 * 80));// 设置某一列的列宽
							}
							cell.setCellStyle(style);
							cell.setCellValue(str);
						}
					}
				}
			}

			sheet.shiftRows(rows, result.size() + rows, -2);
			System.out.println("WRITE EXCEL REPORT IS OK..");
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			return is;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
}
