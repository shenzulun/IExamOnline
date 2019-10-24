/**
 * File Name: XLSData.java
 * Date: 2019-06-12 16:37:59
 */
package me.belucky.exam.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 功能说明：excel的数据结构
 * @author shenzl
 * @date 2019-06-12
 * @version 1.0
 */
public class XLSData {

	/**
	 * 工作区
	 */
	private String sheetName;
	/**
	 * 有效行数
	 */
	private int row_num;
	/**
	 * 有效列数
	 */
	private int column_num;
	/**
	 * 记录列表(其中的对象为数组类型，一行记录作为一个数组存放)
	 */
	private List<String[]> dataList;
	
	private Workbook book;

	/**
	 * 创建对象(默认读取第一个工作区)
	 * @param is
	 * @throws BiffException
	 * @throws IOException
	 */
	public XLSData(InputStream is) throws BiffException, IOException {
		book = Workbook.getWorkbook(is);
	}

	/**
	 * 创建对象(默认读取第一个工作区)
	 * @param file
	 * @throws BiffException
	 * @throws IOException
	 */
	public XLSData(File file) throws BiffException, IOException {
		book = Workbook.getWorkbook(file);
	}

	public String getSheetName() {
		return sheetName;
	}

	public int getRow_num() {
		return row_num;
	}

	public int getColumn_num() {
		return column_num;
	}

	public List<String[]> getDataList(int sheetInd) {
		Sheet sheet = book.getSheet(sheetInd);
		this.sheetName = sheet.getName();
		this.row_num = sheet.getRows();
		this.column_num = sheet.getColumns();

		dataList = new ArrayList<String[]>();
		for (int row = 0; row < row_num; row++) {
			String[] record = new String[column_num];
			for (int n = 0; n < record.length; n++) {
				record[n] = (String) sheet.getCell(n, row).getContents();
			}
			dataList.add(record);
		}
		return dataList;
	}

	public Workbook getBook() {
		return book;
	}

	public void setBook(Workbook book) {
		this.book = book;
	}

}
