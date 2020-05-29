package com.adep.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adep.dto.OrderDto;
import com.adep.entity.OrderEntity;
import com.adep.repository.OrderRepository;
import com.adep.service.OrderService;
import com.adep.shared.model.AllDataSalesDiscountCategory;
import com.adep.shared.model.AllSalesDiscountProfitValue;
import com.adep.shared.model.AllYearSalesProfitDiscountQuantity;
import com.adep.shared.model.DataCategory;
import com.adep.shared.model.DataCategoryProfitSalesDiscount;
import com.adep.shared.model.DataSalesDiscountProfit;
import com.adep.shared.model.DataValueNameColor;
import com.adep.shared.model.FurnitureDataCategory;
import com.adep.shared.model.OfficeSupplyDataCategory;
import com.adep.shared.model.SalesProfitDiscountQuantity;
import com.adep.shared.model.SalesProfitDiscountValue;
import com.adep.shared.model.SeriesData;
import com.adep.shared.model.TechnologyDataCategory;
import com.adep.shared.model.YearSalesProfitDiscountQuantity;
import com.adep.shared.model.MonthSalesProfitDiscountQuantity;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public OrderDto getOderByOderId(String orderId) throws Exception {

		Optional<OrderEntity> storedOrderEntity = orderRepository.findById(Long.parseLong(orderId));
		if (!storedOrderEntity.isPresent()) {
			throw new Exception("Order does not exit with id " + orderId);
		}
		OrderDto orderDto = new OrderDto();
		BeanUtils.copyProperties(storedOrderEntity.get(), orderDto);

		return orderDto;
	}

	@Override
	public List<SeriesData> getDataSalesDiscountProfit() {

		List<OrderEntity> list = new ArrayList<>();
		Iterable<OrderEntity> orderAll = orderRepository.findAll();
		for (OrderEntity orderEntity : orderAll) {
			list.add(orderEntity);
		}
		DataCategoryProfitSalesDiscount dataCategory = allCountryData(list);

		List<DataSalesDiscountProfit> dataSalesDiscountProfit = new ArrayList<>();
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataAccessories()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataAppliances()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataArt()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataBinders()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataChairs()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataCopiers()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataEnvelopes()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataFasteners()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataFurnishings()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataLabels()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataMachines()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataPaper()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataPhones()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataStorage()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataSupplies()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataTables()));

		List<SeriesData> seriesData = new ArrayList<>();

		SeriesData seriesDataProfit = new SeriesData();
		seriesDataProfit.setName("Profit");

		seriesDataProfit.setData(getDataProfit(dataSalesDiscountProfit));

		SeriesData seriesDataAverageDiscount = new SeriesData();
		seriesDataAverageDiscount.setName("Average Discount");

		seriesDataAverageDiscount.setData(getDataDiscount(dataSalesDiscountProfit));

		SeriesData seriesDataSales = new SeriesData();
		seriesDataSales.setName("Sales");

		seriesDataSales.setData(getDataSales(dataSalesDiscountProfit));

		seriesData.add(seriesDataProfit);
		seriesData.add(seriesDataAverageDiscount);
		seriesData.add(seriesDataSales);

		return seriesData;
	}

	@Override
	public AllSalesDiscountProfitValue getSalesProfitDiscountValueByCountry(String country) {

		if (country.contains("world")) {
			country = ".*";
		}

		List<OrderEntity> list = new ArrayList<>();
		Iterable<OrderEntity> orderAll = orderRepository.findByCountry(country);
		for (OrderEntity orderEntity : orderAll) {
			list.add(orderEntity);
		}

		DataCategoryProfitSalesDiscount dataCategory = allCountryData(list);

		DataCategory dataCategories = dataCategory.getDataCategory();

//		List<DataSalesDiscountProfit> dataSalesDiscountProfits = getAllCategoryData(dataCategory.getDataCategory());
		List<DataSalesDiscountProfit> dataSalesDiscountProfits = getAllCategoryData(dataCategories)
				.getAllDataSalesDiscountProfit();

		List<DataValueNameColor> salesData = getSalesDataValueNameColor(dataSalesDiscountProfits);
		List<DataValueNameColor> profitData = getProfitDataValueNameColor(dataSalesDiscountProfits);
		List<DataValueNameColor> discountData = getDiscountDataValueNameColor(dataSalesDiscountProfits);

		SalesProfitDiscountValue allCountrySalesDiscountProfitData = new SalesProfitDiscountValue();
		allCountrySalesDiscountProfitData.setSalesData(salesData);
		allCountrySalesDiscountProfitData.setDiscountData(discountData);
		allCountrySalesDiscountProfitData.setProfitData(profitData);
		List<String> allSubCategories = new ArrayList<String>();

		System.out.println("All dataSalesDiscountProfits : " + dataSalesDiscountProfits.size());
		for (int i = 0; i < dataSalesDiscountProfits.size(); i++) {
			allSubCategories.add(dataSalesDiscountProfits.get(i).getSubCategory());
		}
		System.out.println(allSubCategories);
		allCountrySalesDiscountProfitData.setSubCategory(allSubCategories);

//		TechnologyDataCategory technologyData = technologyDataCategory(list);
//		TechnologyDataCategory technologyData = dataCategory.getTechnologyDataCategory();

//		List<DataSalesDiscountProfit> technologydataSalesDiscountProfits = getAllTechnologyCategoryData(technologyData);
		List<DataSalesDiscountProfit> technologydataSalesDiscountProfits = getAllCategoryData(
				dataCategory.getDataCategory()).getTechnologyDataSalesDiscountProfit();

		List<DataValueNameColor> technologySalesData = getSalesDataValueNameColor(technologydataSalesDiscountProfits);
		List<DataValueNameColor> technologProfitData = getProfitDataValueNameColor(technologydataSalesDiscountProfits);
		List<DataValueNameColor> technologyDiscountData = getDiscountDataValueNameColor(
				technologydataSalesDiscountProfits);

		SalesProfitDiscountValue technolgySalesDiscountProfitValue = new SalesProfitDiscountValue();
		technolgySalesDiscountProfitValue.setDiscountData(technologyDiscountData);
		technolgySalesDiscountProfitValue.setProfitData(technologProfitData);
		technolgySalesDiscountProfitValue.setSalesData(technologySalesData);
		List<String> technologySubCategories = new ArrayList<String>();
		System.out.println("technologydataSalesDiscountProfits : " + technologydataSalesDiscountProfits.size());
		for (int i = 0; i < technologydataSalesDiscountProfits.size(); i++) {
			technologySubCategories.add(technologydataSalesDiscountProfits.get(i).getSubCategory());
		}
		System.out.println(technologySubCategories);
		technolgySalesDiscountProfitValue.setSubCategory(technologySubCategories);

//		FurnitureDataCategory furnitureDataCategory = furnitureDataCategory(list);
//		FurnitureDataCategory furnitureDataCategory = dataCategory.getFurnitureDataCategory();

//		List<DataSalesDiscountProfit> furnitureDataSalesDiscountProfits = getAllFurnitureCategoryData(furnitureDataCategory);

		List<DataSalesDiscountProfit> furnitureDataSalesDiscountProfits = getAllCategoryData(
				dataCategory.getDataCategory()).getFurnitureDataSalesDiscountProfit();
		List<DataValueNameColor> furnitureSalesData = getSalesDataValueNameColor(furnitureDataSalesDiscountProfits);
		List<DataValueNameColor> furnitureProfitData = getProfitDataValueNameColor(furnitureDataSalesDiscountProfits);
		List<DataValueNameColor> furnitureDiscountData = getDiscountDataValueNameColor(
				furnitureDataSalesDiscountProfits);

		SalesProfitDiscountValue furnitureSalesDiscountProfitValue = new SalesProfitDiscountValue();
		furnitureSalesDiscountProfitValue.setSalesData(furnitureSalesData);
		furnitureSalesDiscountProfitValue.setDiscountData(furnitureDiscountData);
		furnitureSalesDiscountProfitValue.setProfitData(furnitureProfitData);
		System.out.println("furnitureDataSalesDiscountProfits : " + furnitureDataSalesDiscountProfits.size());
		List<String> furnitureSubCategories = new ArrayList<String>();
		for (int i = 0; i < furnitureDataSalesDiscountProfits.size(); i++) {
			furnitureSubCategories.add(furnitureDataSalesDiscountProfits.get(i).getSubCategory());
		}
		System.out.println(furnitureSubCategories);
		furnitureSalesDiscountProfitValue.setSubCategory(furnitureSubCategories);

//		OfficeSupplyDataCategory officeSupplyDataCategory = officeSupplyDataCategory(list);
//		OfficeSupplyDataCategory officeSupplyDataCategory = dataCategory.getOfficeSupplyDataCategory();

//		List<DataSalesDiscountProfit> officeSupplyDataSalesDiscountProfits = getAllOfficeSupplyCategoryData(officeSupplyDataCategory);

		List<DataSalesDiscountProfit> officeSupplyDataSalesDiscountProfits = getAllCategoryData(
				dataCategory.getDataCategory()).getOfficeSupplyDataSalesDiscountProfit();

		List<DataValueNameColor> officeSupplySalesData = getSalesDataValueNameColor(
				officeSupplyDataSalesDiscountProfits);
		List<DataValueNameColor> officeSupplyProfitData = getProfitDataValueNameColor(
				officeSupplyDataSalesDiscountProfits);
		List<DataValueNameColor> officeSupplyDiscountData = getDiscountDataValueNameColor(
				officeSupplyDataSalesDiscountProfits);

		SalesProfitDiscountValue officeSupplySalesDiscountProfitValue = new SalesProfitDiscountValue();
		officeSupplySalesDiscountProfitValue.setSalesData(officeSupplySalesData);
		officeSupplySalesDiscountProfitValue.setDiscountData(officeSupplyDiscountData);
		officeSupplySalesDiscountProfitValue.setProfitData(officeSupplyProfitData);

		List<String> officeSupplySubCategories = new ArrayList<String>();
		System.out.println("officeSupplySubCategories : " + officeSupplyDataSalesDiscountProfits.size());
		for (int i = 0; i < officeSupplyDataSalesDiscountProfits.size(); i++) {
			officeSupplySubCategories.add(officeSupplyDataSalesDiscountProfits.get(i).getSubCategory());
		}
		System.out.println(officeSupplySubCategories);
		officeSupplySalesDiscountProfitValue.setSubCategory(officeSupplySubCategories);

		AllSalesDiscountProfitValue allSalesDiscountProfitData = new AllSalesDiscountProfitValue();
		allSalesDiscountProfitData.setAllSalesDiscountProfitValue(allCountrySalesDiscountProfitData);
		allSalesDiscountProfitData.setTechnolgySalesDiscountProfitValue(technolgySalesDiscountProfitValue);
		allSalesDiscountProfitData.setFurnitureSalesDiscountProfitValue(furnitureSalesDiscountProfitValue);
		allSalesDiscountProfitData.setOfficeSupplySalesDiscountProfitValue(officeSupplySalesDiscountProfitValue);

		return allSalesDiscountProfitData;
	}

	@Override
	public AllYearSalesProfitDiscountQuantity getByYear(String category, String subCategory, String region,
			String segment) {
		String year2011 = "2011";
		String year2012 = "2012";
		String year2013 = "2013";
		String year2014 = "2014";

		List<OrderEntity> year1 = orderRepository.findByYear(year2011, category, subCategory, region, segment);
		List<OrderEntity> year2 = orderRepository.findByYear(year2012, category, subCategory, region, segment);
		List<OrderEntity> year3 = orderRepository.findByYear(year2013, category, subCategory, region, segment);
		List<OrderEntity> year4 = orderRepository.findByYear(year2014, category, subCategory, region, segment);
//		MonthSalesProfitDiscountQuantity monthsYear2011SalesProfitDiscountQuantity = getMonthSalesProfitDiscountQuantity(year1);

		YearSalesProfitDiscountQuantity year2011SalesProfitDiscountQuantity = yearSalesProfitDiscountQuantity(
				getMonthSalesProfitDiscountQuantity(year1));

		YearSalesProfitDiscountQuantity year2012SalesProfitDiscountQuantity = yearSalesProfitDiscountQuantity(
				getMonthSalesProfitDiscountQuantity(year2));
		YearSalesProfitDiscountQuantity year2013SalesProfitDiscountQuantity = yearSalesProfitDiscountQuantity(
				getMonthSalesProfitDiscountQuantity(year3));
		YearSalesProfitDiscountQuantity year2014SalesProfitDiscountQuantity = yearSalesProfitDiscountQuantity(
				getMonthSalesProfitDiscountQuantity(year4));

		List<YearSalesProfitDiscountQuantity> yearsSalesProfitDiscountQuantity = new ArrayList<YearSalesProfitDiscountQuantity>();

		yearsSalesProfitDiscountQuantity.add(year2011SalesProfitDiscountQuantity);
		yearsSalesProfitDiscountQuantity.add(year2012SalesProfitDiscountQuantity);
		yearsSalesProfitDiscountQuantity.add(year2013SalesProfitDiscountQuantity);
		yearsSalesProfitDiscountQuantity.add(year2014SalesProfitDiscountQuantity);

		SalesProfitDiscountQuantity salesProfitDiscountQuantity = totalDataSalesProfitDiscountQuantity(
				yearsSalesProfitDiscountQuantity);

		AllYearSalesProfitDiscountQuantity allYearSalesProfitDiscountQuantity = new AllYearSalesProfitDiscountQuantity();
		allYearSalesProfitDiscountQuantity.setYearsSalesProfitDiscountQuantity(yearsSalesProfitDiscountQuantity);
		allYearSalesProfitDiscountQuantity.setTotalSalesProfitDiscountQuantity(salesProfitDiscountQuantity);

		return allYearSalesProfitDiscountQuantity;
	}

	private SalesProfitDiscountQuantity totalDataSalesProfitDiscountQuantity(
			List<YearSalesProfitDiscountQuantity> yearsSalesProfitDiscountQuantity) {
		Double zeroValue = (double) 0;

		Double profit = zeroValue;
		Double sales = zeroValue;
		Double discount = zeroValue;
		Double quantity = zeroValue;

		for (int i = 0; i < yearsSalesProfitDiscountQuantity.size(); i++) {
			YearSalesProfitDiscountQuantity dataSubSalesDiscountProfit = yearsSalesProfitDiscountQuantity.get(i);
			profit = profit + dataSubSalesDiscountProfit.getTotalData().getProfit();
			sales = sales + dataSubSalesDiscountProfit.getTotalData().getSales();
			discount = discount + dataSubSalesDiscountProfit.getTotalData().getDiscount();
			quantity = quantity + dataSubSalesDiscountProfit.getTotalData().getQuantity();
		}

		SalesProfitDiscountQuantity dataSalesDiscountProfit = new SalesProfitDiscountQuantity();

		dataSalesDiscountProfit.setDiscount(discount);
		dataSalesDiscountProfit.setProfit(profit);
		dataSalesDiscountProfit.setSales(sales);
		dataSalesDiscountProfit.setQuantity(quantity);

		return dataSalesDiscountProfit;

	}

	private List<DataSalesDiscountProfit> getAllOfficeSupplyCategoryData(
			OfficeSupplyDataCategory officeSupplyDataCategory) {

		List<DataSalesDiscountProfit> dataSalesDiscountProfit = new ArrayList<>();
		Double vaueZero = (double) 0;
		if (officeSupplyDataCategory.getDataAppliances().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Appliances");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(officeSupplyDataCategory.getDataAppliances()));
		}
		if (officeSupplyDataCategory.getDataArt().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Arts");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(officeSupplyDataCategory.getDataArt()));
		}
		if (officeSupplyDataCategory.getDataBinders().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Binders");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(officeSupplyDataCategory.getDataBinders()));
		}
		if (officeSupplyDataCategory.getDataArt().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Envelopes");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(officeSupplyDataCategory.getDataEnvelopes()));
		}
		if (officeSupplyDataCategory.getDataFasteners().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Fasteners");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(officeSupplyDataCategory.getDataFasteners()));
		}
		if (officeSupplyDataCategory.getDataLabels().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Labels");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(officeSupplyDataCategory.getDataLabels()));
		}
		if (officeSupplyDataCategory.getDataPaper().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Paper");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(officeSupplyDataCategory.getDataPaper()));
		}

		if (officeSupplyDataCategory.getDataStorage().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Storage");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(officeSupplyDataCategory.getDataStorage()));
		}
		if (officeSupplyDataCategory.getDataSupplies().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Supplies");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(officeSupplyDataCategory.getDataSupplies()));
		}
		return dataSalesDiscountProfit;
	}

	private List<DataSalesDiscountProfit> getAllFurnitureCategoryData(FurnitureDataCategory furnitureData) {

		List<DataSalesDiscountProfit> dataSalesDiscountProfit = new ArrayList<>();
		Double vaueZero = (double) 0;

		if (furnitureData.getDataBookcases().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Bookcases");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Furniture");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(furnitureData.getDataBookcases()));
		}
		if (furnitureData.getDataChairs().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Chairs");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Furniture");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(furnitureData.getDataChairs()));
		}
		if (furnitureData.getDataFurnishings().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Furnishings");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Furniture");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(furnitureData.getDataFurnishings()));
		}
		if (furnitureData.getDataTables().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Tables");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Furniture");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(furnitureData.getDataTables()));
		}

		return dataSalesDiscountProfit;
	}

	private List<DataSalesDiscountProfit> getAllTechnologyCategoryData(TechnologyDataCategory technologyData) {
		List<DataSalesDiscountProfit> dataSalesDiscountProfit = new ArrayList<>();
		Double vaueZero = (double) 0;

		if (technologyData.getDataAccessories().isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Accessories");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Technology");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(technologyData.getDataAccessories()));
		}
		if (technologyData.getDataCopiers().isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Copiers");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Technology");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(technologyData.getDataCopiers()));
		}
		if (technologyData.getDataMachines().isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Machines");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Technology");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(technologyData.getDataMachines()));
		}
		if (technologyData.getDataPhones().isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Phones");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Technology");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(technologyData.getDataPhones()));
		}

		return dataSalesDiscountProfit;
	}

	private AllDataSalesDiscountCategory getAllCategoryData(DataCategory dataCategory) {

		List<DataSalesDiscountProfit> dataSalesDiscountProfit = new ArrayList<>();
		List<DataSalesDiscountProfit> technologyDataSalesDiscountProfit = new ArrayList<>();
		List<DataSalesDiscountProfit> furnitureDataSalesDiscountProfit = new ArrayList<>();
		List<DataSalesDiscountProfit> officeSupplyDataSalesDiscountProfit = new ArrayList<>();
		Double vaueZero = (double) 0;
		if (dataCategory.getDataAccessories().isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Accessories");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Technology");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			technologyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataAccessories()));
			technologyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataAccessories()));
		}

		if (dataCategory.getDataAppliances().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Appliances");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataAppliances()));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataAppliances()));
		}
		if (dataCategory.getDataArt().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Arts");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataArt()));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataArt()));
		}
		if (dataCategory.getDataBookcases().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Bookcases");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Furniture");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			furnitureDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataBookcases()));
			furnitureDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataBookcases()));
		}
		if (dataCategory.getDataBinders().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Binders");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataBinders()));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataBinders()));
		}
		if (dataCategory.getDataChairs().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Chairs");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Furniture");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			furnitureDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataChairs()));
			furnitureDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataChairs()));
		}
		if (dataCategory.getDataCopiers().isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Copiers");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Technology");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			technologyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCopiers()));
			technologyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCopiers()));
		}
		if (dataCategory.getDataArt().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Envelopes");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataEnvelopes()));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataEnvelopes()));
		}
		if (dataCategory.getDataFasteners().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Fasteners");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataFasteners()));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataFasteners()));
		}

		if (dataCategory.getDataFurnishings().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Furnishings");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Furniture");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			furnitureDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataFurnishings()));
			furnitureDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataFurnishings()));
		}
		if (dataCategory.getDataLabels().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Labels");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataLabels()));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataLabels()));
		}
		if (dataCategory.getDataMachines().isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Machines");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Technology");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			technologyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataMachines()));
			technologyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataMachines()));
		}
		if (dataCategory.getDataPaper().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Paper");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataPaper()));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataPaper()));
		}

		if (dataCategory.getDataPhones().isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Phones");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Technology");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			technologyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataPhones()));
			technologyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataPhones()));
		}
		if (dataCategory.getDataStorage().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Storage");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataStorage()));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataStorage()));
		}
		if (dataCategory.getDataSupplies().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Supplies");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Office Supplies");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataSupplies()));
			officeSupplyDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataSupplies()));
		}
		if (dataCategory.getDataTables().size() == 0) {
			DataSalesDiscountProfit dataSalesDiscountProfits = new DataSalesDiscountProfit();
			dataSalesDiscountProfits.setSubCategory("Tables");
			dataSalesDiscountProfits.setDiscount(vaueZero);
			dataSalesDiscountProfits.setProfit(vaueZero);
			dataSalesDiscountProfits.setSales(vaueZero);
			dataSalesDiscountProfits.setCategory("Furniture");
			List<DataSalesDiscountProfit> accList = new ArrayList<DataSalesDiscountProfit>();
			accList.add(dataSalesDiscountProfits);
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
			furnitureDataSalesDiscountProfit.add(dataSalesProfitDiscount(accList));
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataTables()));
			furnitureDataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataTables()));
		}
		AllDataSalesDiscountCategory allDataSalesDiscountCategory = new AllDataSalesDiscountCategory();
		allDataSalesDiscountCategory.setAllDataSalesDiscountProfit(dataSalesDiscountProfit);
		allDataSalesDiscountCategory.setFurnitureDataSalesDiscountProfit(furnitureDataSalesDiscountProfit);
		allDataSalesDiscountCategory.setOfficeSupplyDataSalesDiscountProfit(officeSupplyDataSalesDiscountProfit);
		allDataSalesDiscountCategory.setTechnologyDataSalesDiscountProfit(technologyDataSalesDiscountProfit);

		return allDataSalesDiscountCategory;
	}

	private List<DataValueNameColor> getSalesDataValueNameColor(
			List<DataSalesDiscountProfit> dataSalesDiscountProfits) {

		List<DataValueNameColor> getDataValueNameColor = new ArrayList<>();

		for (int i = 0; i < dataSalesDiscountProfits.size(); i++) {
			DataSalesDiscountProfit dataSalesDiscountProfit = dataSalesDiscountProfits.get(i);
			DataValueNameColor dataValueNameColor = new DataValueNameColor();
			dataValueNameColor.setY(dataSalesDiscountProfit.getSales());
			dataValueNameColor.setName(dataSalesDiscountProfit.getSubCategory());
			if (dataSalesDiscountProfit.getSales() > 0) {
				dataValueNameColor.setColor("green");
			} else {
				dataValueNameColor.setColor("red");
			}
			getDataValueNameColor.add(dataValueNameColor);
		}

		return getDataValueNameColor;
	}

	private List<DataValueNameColor> getProfitDataValueNameColor(
			List<DataSalesDiscountProfit> dataProfitDiscountProfits) {

		List<DataValueNameColor> getDataValueNameColor = new ArrayList<>();

		for (int i = 0; i < dataProfitDiscountProfits.size(); i++) {
			DataSalesDiscountProfit dataSalesDiscountProfit = dataProfitDiscountProfits.get(i);
			DataValueNameColor dataValueNameColor = new DataValueNameColor();
			dataValueNameColor.setY(dataSalesDiscountProfit.getProfit());
			dataValueNameColor.setName(dataSalesDiscountProfit.getSubCategory());
			if (dataSalesDiscountProfit.getProfit() > 0) {
				dataValueNameColor.setColor("green");
			} else {
				dataValueNameColor.setColor("red");
			}
			getDataValueNameColor.add(dataValueNameColor);
		}

		return getDataValueNameColor;
	}

	private List<DataValueNameColor> getDiscountDataValueNameColor(
			List<DataSalesDiscountProfit> dataSalesDiscountProfits) {

		List<DataValueNameColor> getDataValueNameColor = new ArrayList<>();

		for (int i = 0; i < dataSalesDiscountProfits.size(); i++) {
			DataSalesDiscountProfit dataSalesDiscountProfit = dataSalesDiscountProfits.get(i);
			DataValueNameColor dataValueNameColor = new DataValueNameColor();
			dataValueNameColor.setY(dataSalesDiscountProfit.getDiscount());
			dataValueNameColor.setName(dataSalesDiscountProfit.getSubCategory());
			if (dataSalesDiscountProfit.getDiscount() > 0) {
				dataValueNameColor.setColor("red");
			} else {
				dataValueNameColor.setColor("green");
			}
			getDataValueNameColor.add(dataValueNameColor);
		}

		return getDataValueNameColor;
	}

	private List<Double> getDataSales(List<DataSalesDiscountProfit> dataSalesDiscountProfits) {

		List<Double> dataSales = new ArrayList<>();
		for (int i = 0; i < dataSalesDiscountProfits.size(); i++) {
			DataSalesDiscountProfit dataSalesDiscountProfit = dataSalesDiscountProfits.get(i);
			dataSales.add(dataSalesDiscountProfit.getSales());
		}
		return dataSales;
	}

	private List<Double> getDataDiscount(List<DataSalesDiscountProfit> dataSalesDiscountProfits) {

		List<Double> dataDiscount = new ArrayList<>();
		for (int i = 0; i < dataSalesDiscountProfits.size(); i++) {
			DataSalesDiscountProfit dataSalesDiscountProfit = dataSalesDiscountProfits.get(i);
			dataDiscount.add(dataSalesDiscountProfit.getDiscount());
		}
		return dataDiscount;
	}

	private List<Double> getDataProfit(List<DataSalesDiscountProfit> dataSalesDiscountProfits) {

		List<Double> dataProfit = new ArrayList<>();
		for (int i = 0; i < dataSalesDiscountProfits.size(); i++) {
			DataSalesDiscountProfit dataSalesDiscountProfit = dataSalesDiscountProfits.get(i);
			dataProfit.add(dataSalesDiscountProfit.getProfit());
		}
		return dataProfit;
	}

	private OfficeSupplyDataCategory officeSupplyDataCategory(List<OrderEntity> orders) {

		List<DataSalesDiscountProfit> dataAppliances = new ArrayList<>();
		List<DataSalesDiscountProfit> dataArt = new ArrayList<>();
		List<DataSalesDiscountProfit> dataBinders = new ArrayList<>();
		List<DataSalesDiscountProfit> dataEnvelopes = new ArrayList<>();
		List<DataSalesDiscountProfit> dataFasteners = new ArrayList<>();
		List<DataSalesDiscountProfit> dataLabels = new ArrayList<>();
		List<DataSalesDiscountProfit> dataPaper = new ArrayList<>();
		List<DataSalesDiscountProfit> dataStorage = new ArrayList<>();
		List<DataSalesDiscountProfit> dataSupplies = new ArrayList<>();
		for (int i = 0; i < orders.size(); i++) {
			OrderEntity orderEntity = orders.get(i);
			if (orderEntity.getCategory().contains("Office Supplies")) {
				if (orderEntity.getSubCategory().contains("Appliances")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataAppliances.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Art")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataArt.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Binders")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataBinders.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Envelopes")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataEnvelopes.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Fasteners")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataFasteners.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Labels")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataLabels.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Paper")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataPaper.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Storage")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataStorage.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Supplies")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataSupplies.add(dataSalesDiscountProfit);
				}
			}
		}

		OfficeSupplyDataCategory officeSupplyDataCategory = new OfficeSupplyDataCategory();
		officeSupplyDataCategory.setDataAppliances(dataAppliances);
		officeSupplyDataCategory.setDataArt(dataArt);
		officeSupplyDataCategory.setDataBinders(dataBinders);
		officeSupplyDataCategory.setDataEnvelopes(dataEnvelopes);
		officeSupplyDataCategory.setDataFasteners(dataFasteners);
		officeSupplyDataCategory.setDataLabels(dataLabels);
		officeSupplyDataCategory.setDataPaper(dataPaper);
		officeSupplyDataCategory.setDataStorage(dataStorage);
		officeSupplyDataCategory.setDataSupplies(dataSupplies);

		return officeSupplyDataCategory;
	}

	private FurnitureDataCategory furnitureDataCategory(List<OrderEntity> orders) {
		List<DataSalesDiscountProfit> dataBookcases = new ArrayList<>();
		List<DataSalesDiscountProfit> dataChairs = new ArrayList<>();
		List<DataSalesDiscountProfit> dataFurnishings = new ArrayList<>();
		List<DataSalesDiscountProfit> dataTables = new ArrayList<>();

		for (int i = 0; i < orders.size(); i++) {
			OrderEntity orderEntity = orders.get(i);

			if (orderEntity.getCategory().contains("Furniture")) {
				if (orderEntity.getSubCategory().contains("Bookcases")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataBookcases.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Chairs")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataChairs.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Furnishings")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataFurnishings.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Tables")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataTables.add(dataSalesDiscountProfit);
				}
			}
		}
		FurnitureDataCategory furnitureDataCategory = new FurnitureDataCategory();
		furnitureDataCategory.setDataBookcases(dataBookcases);
		furnitureDataCategory.setDataChairs(dataChairs);
		furnitureDataCategory.setDataFurnishings(dataFurnishings);
		furnitureDataCategory.setDataTables(dataTables);
		return furnitureDataCategory;
	}

	private TechnologyDataCategory technologyDataCategory(List<OrderEntity> orders) {
		List<DataSalesDiscountProfit> dataAccessories = new ArrayList<>();
		List<DataSalesDiscountProfit> dataCopiers = new ArrayList<>();
		List<DataSalesDiscountProfit> dataMachines = new ArrayList<>();
		List<DataSalesDiscountProfit> dataPhones = new ArrayList<>();
		for (int i = 0; i < orders.size(); i++) {
			OrderEntity orderEntity = orders.get(i);

			if (orderEntity.getCategory().contains("Technology")) {
				if (orderEntity.getSubCategory().contains("Accessories")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataAccessories.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Copiers")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataCopiers.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Machines")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataMachines.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Phones")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataPhones.add(dataSalesDiscountProfit);
				}
			}
		}
		TechnologyDataCategory technologyDataCategory = new TechnologyDataCategory();
		technologyDataCategory.setDataAccessories(dataAccessories);
		technologyDataCategory.setDataCopiers(dataCopiers);
		technologyDataCategory.setDataMachines(dataMachines);
		technologyDataCategory.setDataPhones(dataPhones);

		return technologyDataCategory;
	}

	private DataCategoryProfitSalesDiscount allCountryData(List<OrderEntity> orders) {

		List<DataSalesDiscountProfit> dataAccessories = new ArrayList<>();
		List<DataSalesDiscountProfit> dataAppliances = new ArrayList<>();
		List<DataSalesDiscountProfit> dataArt = new ArrayList<>();
		List<DataSalesDiscountProfit> dataBinders = new ArrayList<>();
		List<DataSalesDiscountProfit> dataBookcases = new ArrayList<>();
		List<DataSalesDiscountProfit> dataChairs = new ArrayList<>();
		List<DataSalesDiscountProfit> dataCopiers = new ArrayList<>();
		List<DataSalesDiscountProfit> dataEnvelopes = new ArrayList<>();
		List<DataSalesDiscountProfit> dataFasteners = new ArrayList<>();
		List<DataSalesDiscountProfit> dataFurnishings = new ArrayList<>();
		List<DataSalesDiscountProfit> dataLabels = new ArrayList<>();
		List<DataSalesDiscountProfit> dataMachines = new ArrayList<>();
		List<DataSalesDiscountProfit> dataPaper = new ArrayList<>();
		List<DataSalesDiscountProfit> dataPhones = new ArrayList<>();
		List<DataSalesDiscountProfit> dataStorage = new ArrayList<>();
		List<DataSalesDiscountProfit> dataSupplies = new ArrayList<>();
		List<DataSalesDiscountProfit> dataTables = new ArrayList<>();

		for (int i = 0; i < orders.size(); i++) {
			OrderEntity orderEntity = orders.get(i);

			if (orderEntity.getCategory().contains("Technology")) {
				if (orderEntity.getSubCategory().contains("Accessories")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataAccessories.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Copiers")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataCopiers.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Machines")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataMachines.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Phones")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataPhones.add(dataSalesDiscountProfit);
				}
			} else if (orderEntity.getCategory().contains("Furniture")) {
				if (orderEntity.getSubCategory().contains("Bookcases")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataBookcases.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Chairs")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataChairs.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Furnishings")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataFurnishings.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Tables")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataTables.add(dataSalesDiscountProfit);
				}
			} else if (orderEntity.getCategory().contains("Office Supplies")) {
				if (orderEntity.getSubCategory().contains("Appliances")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataAppliances.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Art")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataArt.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Binders")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataBinders.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Envelopes")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataEnvelopes.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Fasteners")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataFasteners.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Labels")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataLabels.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Paper")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataPaper.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Storage")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataStorage.add(dataSalesDiscountProfit);
				} else if (orderEntity.getSubCategory().contains("Supplies")) {
					DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
					BeanUtils.copyProperties(orderEntity, dataSalesDiscountProfit);
					dataSupplies.add(dataSalesDiscountProfit);
				}
			}
		}

		DataCategory dataCategory = new DataCategory();

		dataCategory.setDataAccessories(dataAccessories);
		dataCategory.setDataAppliances(dataAppliances);
		dataCategory.setDataArt(dataArt);
		dataCategory.setDataBinders(dataBinders);
		dataCategory.setDataBookcases(dataBookcases);
		dataCategory.setDataChairs(dataChairs);
		dataCategory.setDataCopiers(dataCopiers);
		dataCategory.setDataEnvelopes(dataEnvelopes);
		dataCategory.setDataFasteners(dataFasteners);
		dataCategory.setDataFurnishings(dataFurnishings);
		dataCategory.setDataLabels(dataLabels);
		dataCategory.setDataMachines(dataMachines);
		dataCategory.setDataPaper(dataPaper);
		dataCategory.setDataPhones(dataPhones);
		dataCategory.setDataStorage(dataStorage);
		dataCategory.setDataSupplies(dataSupplies);
		dataCategory.setDataTables(dataTables);

		TechnologyDataCategory technologyDataCategory = new TechnologyDataCategory();
		technologyDataCategory.setDataAccessories(dataAccessories);
		technologyDataCategory.setDataCopiers(dataCopiers);
		technologyDataCategory.setDataMachines(dataMachines);
		technologyDataCategory.setDataPhones(dataPhones);

		FurnitureDataCategory furnitureDataCategory = new FurnitureDataCategory();
		furnitureDataCategory.setDataBookcases(dataBookcases);
		furnitureDataCategory.setDataChairs(dataChairs);
		furnitureDataCategory.setDataFurnishings(dataFurnishings);
		furnitureDataCategory.setDataTables(dataTables);

		OfficeSupplyDataCategory officeSupplyDataCategory = new OfficeSupplyDataCategory();
		officeSupplyDataCategory.setDataAppliances(dataAppliances);
		officeSupplyDataCategory.setDataArt(dataArt);
		officeSupplyDataCategory.setDataBinders(dataBinders);
		officeSupplyDataCategory.setDataEnvelopes(dataEnvelopes);
		officeSupplyDataCategory.setDataFasteners(dataFasteners);
		officeSupplyDataCategory.setDataLabels(dataLabels);
		officeSupplyDataCategory.setDataPaper(dataPaper);
		officeSupplyDataCategory.setDataStorage(dataStorage);
		officeSupplyDataCategory.setDataSupplies(dataSupplies);

		DataCategoryProfitSalesDiscount dataCategoryProfitSalesDiscount = new DataCategoryProfitSalesDiscount();
		dataCategoryProfitSalesDiscount.setDataCategory(dataCategory);
		dataCategoryProfitSalesDiscount.setFurnitureDataCategory(furnitureDataCategory);
		dataCategoryProfitSalesDiscount.setOfficeSupplyDataCategory(officeSupplyDataCategory);
		dataCategoryProfitSalesDiscount.setTechnologyDataCategory(technologyDataCategory);

		return dataCategoryProfitSalesDiscount;
	}

	private DataSalesDiscountProfit dataSalesProfitDiscount(List<DataSalesDiscountProfit> dataSubCategory) {

		Double zeroValue = (double) 0;
		Double profit = zeroValue;
		Double sales = zeroValue;
		Double discount = zeroValue;
		Double avgDiscount = zeroValue;

		int totalData = dataSubCategory.size();
		String subCategory = "";
		String category = "";

		for (int i = 0; i < totalData; i++) {

			DataSalesDiscountProfit dataSubSalesDiscountProfit = dataSubCategory.get(i);

			profit = profit + dataSubSalesDiscountProfit.getProfit();
			sales = sales + dataSubSalesDiscountProfit.getSales();
			discount = discount + dataSubSalesDiscountProfit.getDiscount();

		}
		if (totalData > 0) {
			avgDiscount = discount / dataSubCategory.size();
			subCategory = dataSubCategory.get(0).getSubCategory();
			category = dataSubCategory.get(0).getCategory();
		}
		DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();

		dataSalesDiscountProfit.setDiscount(avgDiscount);
		dataSalesDiscountProfit.setProfit(profit);
		dataSalesDiscountProfit.setSales(sales);
		dataSalesDiscountProfit.setSubCategory(subCategory);
		dataSalesDiscountProfit.setCategory(category);

		return dataSalesDiscountProfit;
	}

	private SalesProfitDiscountQuantity monthSalesProfitDiscountQuantity(List<SalesProfitDiscountQuantity> dataMonth) {

		Double zeroValue = (double) 0;

		Double profit = zeroValue;
		Double sales = zeroValue;
		Double discount = zeroValue;
		Double quantity = zeroValue;

		for (int i = 0; i < dataMonth.size(); i++) {

			SalesProfitDiscountQuantity dataSubSalesDiscountProfit = dataMonth.get(i);

			profit = profit + dataSubSalesDiscountProfit.getProfit();
			sales = sales + dataSubSalesDiscountProfit.getSales();
			discount = discount + dataSubSalesDiscountProfit.getDiscount();
			quantity = quantity + dataSubSalesDiscountProfit.getQuantity();
		}

		SalesProfitDiscountQuantity dataSalesDiscountProfit = new SalesProfitDiscountQuantity();

		dataSalesDiscountProfit.setDiscount(discount);
		dataSalesDiscountProfit.setProfit(profit);
		dataSalesDiscountProfit.setSales(sales);
		dataSalesDiscountProfit.setQuantity(quantity);

		return dataSalesDiscountProfit;
	}

	private YearSalesProfitDiscountQuantity yearSalesProfitDiscountQuantity(
			MonthSalesProfitDiscountQuantity monthsYearSalesProfitDiscountQuantity) {

		List<SalesProfitDiscountQuantity> dataYear = new ArrayList<SalesProfitDiscountQuantity>();

		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataJan()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataFeb()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataMar()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataApr()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataMay()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataJun()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataJul()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataAug()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataSep()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataOct()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataNov()));
		dataYear.add(monthSalesProfitDiscountQuantity(monthsYearSalesProfitDiscountQuantity.getDataDec()));

		YearSalesProfitDiscountQuantity yearSalesProfitDiscountQuantity = new YearSalesProfitDiscountQuantity();
		yearSalesProfitDiscountQuantity.setDataYear(dataYear);

		SalesProfitDiscountQuantity totalYearData = new SalesProfitDiscountQuantity();

		Double zeroValue = (double) 0;

		Double profit = zeroValue;
		Double sales = zeroValue;
		Double discount = zeroValue;
		Double quantity = zeroValue;
		for (int i = 0; i < dataYear.size(); i++) {
			profit = profit + dataYear.get(i).getProfit();
			sales = sales + dataYear.get(i).getSales();
			discount = discount + dataYear.get(i).getDiscount();
			quantity = quantity + dataYear.get(i).getQuantity();
		}
		totalYearData.setDiscount(discount);
		totalYearData.setProfit(profit);
		totalYearData.setQuantity(quantity);
		totalYearData.setSales(sales);
		yearSalesProfitDiscountQuantity.setTotalData(totalYearData);

		return yearSalesProfitDiscountQuantity;
	}

	private MonthSalesProfitDiscountQuantity getMonthSalesProfitDiscountQuantity(List<OrderEntity> orders) {

		List<SalesProfitDiscountQuantity> dataJan = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataFeb = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataMar = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataApr = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataMay = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataJun = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataJul = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataAug = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataSep = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataOct = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataNov = new ArrayList<>();
		List<SalesProfitDiscountQuantity> dataDec = new ArrayList<>();

		for (int i = 0; i < orders.size(); i++) {
			OrderEntity orderEntity = orders.get(i);
			LocalDate localDate = orderEntity.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if (1 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataJan.add(salesProfitDiscountQuantity);
			} else if (2 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataFeb.add(salesProfitDiscountQuantity);
			} else if (3 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataMar.add(salesProfitDiscountQuantity);
			} else if (4 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataApr.add(salesProfitDiscountQuantity);
			} else if (5 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataMay.add(salesProfitDiscountQuantity);
			} else if (6 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataJun.add(salesProfitDiscountQuantity);
			} else if (7 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataJul.add(salesProfitDiscountQuantity);
			} else if (8 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataAug.add(salesProfitDiscountQuantity);
			} else if (9 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataSep.add(salesProfitDiscountQuantity);
			} else if (10 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataOct.add(salesProfitDiscountQuantity);
			} else if (11 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataNov.add(salesProfitDiscountQuantity);
			} else if (12 == localDate.getMonthValue()) {
				SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
				BeanUtils.copyProperties(orderEntity, salesProfitDiscountQuantity);
				dataDec.add(salesProfitDiscountQuantity);
			}
		}

		if (dataJan.isEmpty()) {

		}

		MonthSalesProfitDiscountQuantity monthSalesProfitDiscountQuantity = new MonthSalesProfitDiscountQuantity();

		SalesProfitDiscountQuantity salesProfitDiscountQuantity = new SalesProfitDiscountQuantity();
		Double zeroValue = (double) 0;

		Double profit = zeroValue;
		Double sales = zeroValue;
		Double discount = zeroValue;
		Double quantity = zeroValue;
		salesProfitDiscountQuantity.setDiscount(discount);
		salesProfitDiscountQuantity.setProfit(profit);
		salesProfitDiscountQuantity.setSales(sales);
		salesProfitDiscountQuantity.setQuantity(quantity);
		if (dataJan.isEmpty()) {
			dataJan.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataJan(dataJan);
		} else {
			monthSalesProfitDiscountQuantity.setDataJan(dataJan);
		}
		if (dataFeb.isEmpty()) {
			dataFeb.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataFeb(dataFeb);
		} else {
			monthSalesProfitDiscountQuantity.setDataFeb(dataFeb);
		}
		if (dataMar.isEmpty()) {
			dataMar.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataMar(dataMar);
		} else {

			monthSalesProfitDiscountQuantity.setDataMar(dataMar);
		}
		if (dataApr.isEmpty()) {
			dataApr.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataApr(dataApr);
		} else {
			monthSalesProfitDiscountQuantity.setDataApr(dataApr);
		}
		if (dataMay.isEmpty()) {
			dataMay.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataMay(dataMay);
		} else {
			monthSalesProfitDiscountQuantity.setDataMay(dataMay);
		}
		if (dataJun.isEmpty()) {
			dataJun.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataJun(dataJun);
		} else {
			monthSalesProfitDiscountQuantity.setDataJun(dataJun);
		}
		if (dataJul.isEmpty()) {
			dataJul.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataJul(dataJul);
		} else {
			monthSalesProfitDiscountQuantity.setDataJul(dataJul);
		}
		if (dataAug.isEmpty()) {
			dataAug.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataAug(dataAug);
		} else {
			monthSalesProfitDiscountQuantity.setDataAug(dataAug);
		}
		if (dataSep.isEmpty()) {
			dataSep.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataSep(dataSep);
		} else {
			monthSalesProfitDiscountQuantity.setDataSep(dataSep);
		}
		if (dataOct.isEmpty()) {
			dataOct.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataOct(dataOct);
		} else {
			monthSalesProfitDiscountQuantity.setDataOct(dataOct);
		}
		if (dataNov.isEmpty()) {
			dataNov.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataNov(dataNov);
		} else {
			monthSalesProfitDiscountQuantity.setDataNov(dataNov);
		}
		if (dataDec.isEmpty()) {
			dataDec.add(salesProfitDiscountQuantity);
			monthSalesProfitDiscountQuantity.setDataDec(dataDec);
		} else {
			monthSalesProfitDiscountQuantity.setDataDec(dataDec);
		}

		return monthSalesProfitDiscountQuantity;
	}

}