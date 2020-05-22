package com.adep;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.adep.entity.OrderEntity;
import com.adep.repository.OrderRepository;

//@Component // #comment this line
public class InitialDataSetup {

//	@Autowired // #comment this line
	private OrderRepository orderRepository;

//	@EventListener // #comment this line
	public void onApplicationEvent(ApplicationReadyEvent event) throws IOException, ParseException {
		System.out.println("Starting application");

		// Absolute path of excel
		String excelFilePath = "G:\\Infy\\ADEP\\work_space\\assignment2\\SuperStore.xlsx";
		orderEntityData(excelFilePath);

	}

	/*
	 * Extract excel sheet data into list of list string;
	 */

	private List<List<String>> getSheetDataList(Sheet sheet) {
		List<List<String>> ret = new ArrayList<List<String>>();
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum > 0) {
			for (int i = firstRowNum; i < lastRowNum + 1; i++) {
				Row row = sheet.getRow(i);
				int firstCellNum = row.getFirstCellNum();
				int lastCellNum = row.getLastCellNum();
				List<String> rowDataList = new ArrayList<String>();
				for (int j = firstCellNum; j < lastCellNum; j++) {
					try {
						Cell cell = row.getCell(j);
						if(cell==null) {
							String cellValue = "";
							rowDataList.add(cellValue);	
						}
						int cellType = cell.getCellType().getCode();
						if (cellType == CellType.NUMERIC.getCode()) {
							double numberValue = cell.getNumericCellValue();
							String stringCellValue = BigDecimal.valueOf(numberValue).toPlainString();
							rowDataList.add(stringCellValue);
						} else if (cellType == CellType.STRING.getCode()) {
							String cellValue = cell.getStringCellValue();
							rowDataList.add(cellValue);
						}
					} catch (Exception e) {
					}
				}
				ret.add(rowDataList);
			}
		}
		return ret;
	}

	/*
	 * save extracted sheet data into database
	 */

	public void orderEntityData(String excelFilePath) throws IOException, ParseException {
		FileInputStream fInputStream = new FileInputStream(excelFilePath.trim());
		Workbook excelWorkBook = new XSSFWorkbook(fInputStream);

		// Set sheet no.
		Sheet sheet = excelWorkBook.getSheetAt(0);
		List<List<String>> dataList = getSheetDataList(sheet);

		List<OrderEntity> orderEntities = new ArrayList<>();
		List<String> headerRow1 = dataList.get(0);
		System.out.println("Header Row :" + headerRow1);
		int rowCount1 = dataList.size();
		System.out.println("Total row count : " + rowCount1);

		if (dataList != null) {
			for (int i = 1; i < rowCount1; i++) {
				List<String> dataRow = dataList.get(i);
				OrderEntity orderEntity = new OrderEntity();
				orderEntity.setRowId(Double.parseDouble(dataRow.get(0)));
				orderEntity.setOrderId(dataRow.get(1));
				Date orderDate= DateUtil.getJavaDate(Double.parseDouble(dataRow.get(2)));
				orderEntity.setOrderDate(orderDate);
				Date shipDate= DateUtil.getJavaDate(Double.parseDouble(dataRow.get(3)));
				orderEntity.setShipDate(shipDate);
				orderEntity.setShipMode(dataRow.get(4));
				orderEntity.setCustomerId(dataRow.get(5));
				orderEntity.setCustomerName(dataRow.get(6));
				orderEntity.setSegment(dataRow.get(7));
				orderEntity.setCity(dataRow.get(8));
				orderEntity.setState(dataRow.get(9));
				orderEntity.setCountry(dataRow.get(10));
				orderEntity.setPostalCode(dataRow.get(11));
				orderEntity.setMarket(dataRow.get(12));
				orderEntity.setRegion(dataRow.get(13));
				orderEntity.setProductId(dataRow.get(14));
				orderEntity.setCategory(dataRow.get(5));
				orderEntity.setSubCategory(dataRow.get(16));
				orderEntity.setProductName(dataRow.get(17));
				orderEntity.setSales(Double.parseDouble(dataRow.get(18)));
				orderEntity.setQuantity(Double.parseDouble(dataRow.get(19)));
				orderEntity.setDiscount(Double.parseDouble(dataRow.get(20)));
				orderEntity.setProfit(Double.parseDouble(dataRow.get(21)));
				orderEntity.setShippingCost(Double.parseDouble(dataRow.get(22)));
				orderEntity.setOrderPriority(dataRow.get(23));

				orderEntities.add(orderEntity);
//				System.out.println(str);
			}
		}
		System.out.println("Executing : " + dataList.size());
//		orderRepository.saveAll(orderEntities); //#comment this line
		System.out.println("Done with : " + dataList.size()+" records");
	}

	/*
	 * Extract excel sheet data into list of list objects;
	 */

	private static List<List<Object>> getSheetDataListObject(Sheet sheet) {
		List<List<Object>> ret = new ArrayList<List<Object>>();
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum > 0) {
			for (int i = firstRowNum; i < lastRowNum + 1; i++) {
				Row row = sheet.getRow(i);
				int firstCellNum = row.getFirstCellNum();
				int lastCellNum = row.getLastCellNum();
				List<Object> rowDataList = new ArrayList<Object>();
				for (int j = firstCellNum; j < lastCellNum; j++) {
					try {
						Cell cell = row.getCell(j);
						int cellType = cell.getCellType().getCode();
						if (cellType == CellType.NUMERIC.getCode()) {
							double numberValue = cell.getNumericCellValue();
							String stringCellValue = BigDecimal.valueOf(numberValue).toPlainString();
							rowDataList.add(stringCellValue);
						} else if (cellType == CellType.STRING.getCode()) {
							String cellValue = cell.getStringCellValue();
							rowDataList.add(cellValue);
						}else {
							String cellValue = cell.getStringCellValue();
							rowDataList.add(cellValue);
						}

					} catch (Exception e) {
					}
				}
				ret.add(rowDataList);
			}
		}
		return ret;
	}

}
