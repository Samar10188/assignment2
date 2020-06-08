package com.adep.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adep.dto.OrderDto;
import com.adep.entity.OrderEntity;
import com.adep.repository.OrderRepository;
import com.adep.service.OrderService;
import com.adep.shared.model.AllCountrySalesReturn;
import com.adep.shared.model.AllDataSalesDiscountCategory;
import com.adep.shared.model.AllSalesDiscountProfitValue;
import com.adep.shared.model.AllYearSalesProfitDiscountQuantity;
import com.adep.shared.model.CountrySales;
import com.adep.shared.model.CountrySalesReturn;
import com.adep.shared.model.DataCategory;
import com.adep.shared.model.DataCategoryProfitSalesDiscount;
import com.adep.shared.model.DataSalesDiscountProfit;
import com.adep.shared.model.DataValueNameColor;
import com.adep.shared.model.FurnitureDataCategory;
import com.adep.shared.model.OfficeSupplyDataCategory;
import com.adep.shared.model.RegionSalesDiscountProfit;
import com.adep.shared.model.SalesDiscountProfit;
import com.adep.shared.model.SalesDiscountProfitSubCategory;
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

	@Transactional
	@Override
	public AllCountrySalesReturn findRetunedOrder(int top) {
	
		List<Object[]> returned = orderRepository.findRetunedOrder();
		Double zeroValue = (double) 0;
		Double totalReturnedSales = zeroValue;
		List<CountrySales> dataReturnedCountrySales = new ArrayList<CountrySales>();
		for (int i = 0; i < returned.size(); i++) {
			Object[] returnedCountrySales = returned.get(i);
			CountrySales countrySale = new CountrySales();
			countrySale.setCountry(String.valueOf(returnedCountrySales[0]));
			Double returnSales = Double.parseDouble(String.valueOf(returnedCountrySales[1]));
			countrySale.setSales(returnSales);
			totalReturnedSales = totalReturnedSales + returnSales;
			dataReturnedCountrySales.add(countrySale);
		}

		Collections.sort(dataReturnedCountrySales, Comparator.comparing(CountrySales::getCountry));

		List<Object[]> sales = orderRepository.findCounrtySales();

		Double totalSales = zeroValue;
		List<CountrySales> dataCountrySales = new ArrayList<CountrySales>();
		for (int i = 0; i < sales.size(); i++) {
			Object[] countrySales = sales.get(i);
			CountrySales countrySale = new CountrySales();
			countrySale.setCountry(String.valueOf(countrySales[0]));
			Double sale = Double.parseDouble(String.valueOf(countrySales[1]));
			countrySale.setSales(sale);
			totalSales = totalSales + sale;
			dataCountrySales.add(countrySale);
		}

		List<CountrySalesReturn> countrySalesReturns = new ArrayList<CountrySalesReturn>();
		for (CountrySales country : dataReturnedCountrySales) {
			for (CountrySales country2 : dataCountrySales) {
				if (country.getCountry().equalsIgnoreCase(country2.getCountry())) {
					CountrySalesReturn countrySalesReturn = new CountrySalesReturn();
					countrySalesReturn.setCountry(country.getCountry());
					countrySalesReturn.setReturnSales(country.getSales());
					countrySalesReturn.setSales(country2.getSales());
					countrySalesReturns.add(countrySalesReturn);
				}
			}
		}

		Collections.sort(countrySalesReturns, Comparator.comparing(CountrySalesReturn::getReturnSales).reversed());

		Double totalTopReturnedSales = zeroValue;
		Double totalTopSales = zeroValue;

		List<CountrySalesReturn> topCountrySalesReturn = new ArrayList<>();
		if (top > countrySalesReturns.size() || top < 0) {
			top = countrySalesReturns.size();
		}
		for (int i = 0; i < top; i++) {
			totalTopReturnedSales = totalTopReturnedSales + countrySalesReturns.get(i).getReturnSales();
			totalTopSales = totalTopSales + countrySalesReturns.get(i).getSales();
			topCountrySalesReturn.add(countrySalesReturns.get(i));
		}

		System.out.println("Main list size : " + topCountrySalesReturn.size());

		AllCountrySalesReturn allCountrySalesReturn = new AllCountrySalesReturn();

		allCountrySalesReturn.setCountrySalesReturns(topCountrySalesReturn);
		allCountrySalesReturn.setTotalReturnedSales(totalReturnedSales);
		allCountrySalesReturn.setTotalSales(totalSales);
		allCountrySalesReturn.setTotalTopReturnedSales(totalTopReturnedSales);
		allCountrySalesReturn.setTotalTopSales(totalTopSales);

		return allCountrySalesReturn;
	}

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

	@Transactional
	@Override
	public RegionSalesDiscountProfit getDataSubCategorySalesDiscountProfit(int top) {

		List<String> regions = new ArrayList<>();
		regions.add("East");
		regions.add("Oceania");
		regions.add("Central");
		regions.add("Africa");
		regions.add("West");
		regions.add("South");
		regions.add("Central Asia");
		regions.add("EMEA");
		regions.add("North Asia");
		regions.add("North");
		regions.add("Caribbean");
		regions.add("Southeast Asia");
		regions.add("Canada");

		List<SalesDiscountProfitSubCategory> regionEast = getRegionTopData(regions.get(0), top);
		List<SalesDiscountProfitSubCategory> regionOceania = getRegionTopData(regions.get(1), top);
		List<SalesDiscountProfitSubCategory> regionCentral = getRegionTopData(regions.get(2), top);
		List<SalesDiscountProfitSubCategory> regionAfrica = getRegionTopData(regions.get(3), top);
		List<SalesDiscountProfitSubCategory> regionWest = getRegionTopData(regions.get(4), top);
		List<SalesDiscountProfitSubCategory> regionSouth = getRegionTopData(regions.get(5), top);
		List<SalesDiscountProfitSubCategory> regionCentralAsia = getRegionTopData(regions.get(6), top);
		List<SalesDiscountProfitSubCategory> regionEMEA = getRegionTopData(regions.get(7), top);
		List<SalesDiscountProfitSubCategory> regionNorthAsia = getRegionTopData(regions.get(8), top);
		List<SalesDiscountProfitSubCategory> regionNorth = getRegionTopData(regions.get(9), top);
		List<SalesDiscountProfitSubCategory> regionCaribbean = getRegionTopData(regions.get(10), top);
		List<SalesDiscountProfitSubCategory> regionSoutheastAsia = getRegionTopData(regions.get(11), top);
		List<SalesDiscountProfitSubCategory> regionCanada = getRegionTopData(regions.get(12), top);

		RegionSalesDiscountProfit regionSalesDiscountProfit = new RegionSalesDiscountProfit();
		regionSalesDiscountProfit.setRegionAfrica(regionAfrica);
		regionSalesDiscountProfit.setRegionEast(regionEast);
		regionSalesDiscountProfit.setRegionOceania(regionOceania);
		regionSalesDiscountProfit.setRegionCentral(regionCentral);
		regionSalesDiscountProfit.setRegionAfrica(regionAfrica);
		regionSalesDiscountProfit.setRegionWest(regionWest);
		regionSalesDiscountProfit.setRegionSouth(regionSouth);
		regionSalesDiscountProfit.setRegionCentralAsia(regionCentralAsia);
		regionSalesDiscountProfit.setRegionEMEA(regionEMEA);
		regionSalesDiscountProfit.setRegionNorthAsia(regionNorthAsia);
		regionSalesDiscountProfit.setRegionNorth(regionNorth);
		regionSalesDiscountProfit.setRegionCaribbean(regionCaribbean);
		regionSalesDiscountProfit.setRegionSoutheastAsia(regionSoutheastAsia);
		regionSalesDiscountProfit.setRegionCanada(regionCanada);

		return regionSalesDiscountProfit;
	}

	@Transactional
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

	@Override
	public AllSalesDiscountProfitValue getSalesProfitDiscountValueByCountry(String country) {

		List<OrderEntity> list = new ArrayList<>();
		Iterable<OrderEntity> orderAll = orderRepository.findByCountry(country);
		for (OrderEntity orderEntity : orderAll) {
			list.add(orderEntity);
		}

		DataCategoryProfitSalesDiscount dataCategory = allCountryData(list);

		DataCategory dataCategories = dataCategory.getDataCategory();

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
		for (int i = 0; i < dataSalesDiscountProfits.size(); i++) {
			allSubCategories.add(dataSalesDiscountProfits.get(i).getSubCategory());
		}
		allCountrySalesDiscountProfitData.setSubCategory(allSubCategories);

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

		for (int i = 0; i < technologydataSalesDiscountProfits.size(); i++) {
			technologySubCategories.add(technologydataSalesDiscountProfits.get(i).getSubCategory());
		}

		technolgySalesDiscountProfitValue.setSubCategory(technologySubCategories);

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

		List<String> furnitureSubCategories = new ArrayList<String>();
		for (int i = 0; i < furnitureDataSalesDiscountProfits.size(); i++) {
			furnitureSubCategories.add(furnitureDataSalesDiscountProfits.get(i).getSubCategory());
		}

		furnitureSalesDiscountProfitValue.setSubCategory(furnitureSubCategories);

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
		for (int i = 0; i < officeSupplyDataSalesDiscountProfits.size(); i++) {
			officeSupplySubCategories.add(officeSupplyDataSalesDiscountProfits.get(i).getSubCategory());
		}
		officeSupplySalesDiscountProfitValue.setSubCategory(officeSupplySubCategories);

		AllSalesDiscountProfitValue allSalesDiscountProfitData = new AllSalesDiscountProfitValue();
		allSalesDiscountProfitData.setAllSalesDiscountProfitValue(allCountrySalesDiscountProfitData);
		allSalesDiscountProfitData.setTechnolgySalesDiscountProfitValue(technolgySalesDiscountProfitValue);
		allSalesDiscountProfitData.setFurnitureSalesDiscountProfitValue(furnitureSalesDiscountProfitValue);
		allSalesDiscountProfitData.setOfficeSupplySalesDiscountProfitValue(officeSupplySalesDiscountProfitValue);

		return allSalesDiscountProfitData;
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

		seriesDataProfit.setData(getDataSalesDiscountProfit(dataSalesDiscountProfit).getDataProfit());

		SeriesData seriesDataAverageDiscount = new SeriesData();
		seriesDataAverageDiscount.setName("Average Discount");

		seriesDataAverageDiscount.setData(getDataSalesDiscountProfit(dataSalesDiscountProfit).getDataDiscount());

		SeriesData seriesDataSales = new SeriesData();
		seriesDataSales.setName("Sales");

		seriesDataSales.setData(getDataSalesDiscountProfit(dataSalesDiscountProfit).getDataSales());

		seriesData.add(seriesDataProfit);
		seriesData.add(seriesDataAverageDiscount);
		seriesData.add(seriesDataSales);

		return seriesData;
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

	private List<SalesDiscountProfitSubCategory> getRegionTopData(String region, int top) {

		List<OrderEntity> listEast = new ArrayList<>();
		Iterable<OrderEntity> orderAll = orderRepository.findByRegion(region);
		for (OrderEntity orderEntity : orderAll) {
			listEast.add(orderEntity);
		}

		DataCategoryProfitSalesDiscount dataCategoryRegionEast = allCountryData1(listEast);
		List<SalesDiscountProfitSubCategory> profitRegionEast = getRegionSubCategoryData(dataCategoryRegionEast);

		Collections.sort(profitRegionEast, Comparator.comparing(SalesDiscountProfitSubCategory::getProfit).reversed());

		List<SalesDiscountProfitSubCategory> topProfitRegion = new ArrayList<>();

		if (top > profitRegionEast.size() || top < 0) {
			top = profitRegionEast.size();
		}

		for (int i = 0; i < top; i++) {
			topProfitRegion.add(profitRegionEast.get(i));
		}

		return topProfitRegion;
	}

	private List<SalesDiscountProfitSubCategory> getRegionSubCategoryData(
			DataCategoryProfitSalesDiscount dataCategory) {

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

		SalesDiscountProfitSubCategory valueAccessory = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataAccessories()),
				valueAccessory);

		SalesDiscountProfitSubCategory valueAppliance = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataAppliances()),
				valueAppliance);

		SalesDiscountProfitSubCategory valueArt = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataArt()), valueArt);

		SalesDiscountProfitSubCategory valueBinder = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataBinders()), valueBinder);

		SalesDiscountProfitSubCategory valueChair = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataChairs()), valueChair);

		SalesDiscountProfitSubCategory valueCopier = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataCopiers()), valueCopier);

		SalesDiscountProfitSubCategory valueEnvelope = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataEnvelopes()),
				valueEnvelope);

		SalesDiscountProfitSubCategory valueFastener = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataFasteners()),
				valueFastener);

		SalesDiscountProfitSubCategory valueFurnishing = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataFurnishings()),
				valueFurnishing);

		SalesDiscountProfitSubCategory valueLabels = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataLabels()), valueLabels);

		SalesDiscountProfitSubCategory valueMachines = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataMachines()),
				valueMachines);

		SalesDiscountProfitSubCategory valuePaper = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataPaper()), valuePaper);

		SalesDiscountProfitSubCategory valuePhones = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataPhones()), valuePhones);

		SalesDiscountProfitSubCategory valueStorage = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataStorage()),
				valueStorage);

		SalesDiscountProfitSubCategory valueSupply = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataSupplies()),
				valueSupply);

		SalesDiscountProfitSubCategory valueTable = new SalesDiscountProfitSubCategory();
		BeanUtils.copyProperties(dataSalesProfitDiscount(dataCategory.getDataCategory().getDataTables()), valueTable);

		List<SalesDiscountProfitSubCategory> salesDiscountProfitSubCategory = new ArrayList<SalesDiscountProfitSubCategory>();

		salesDiscountProfitSubCategory.add(valueAccessory);
		salesDiscountProfitSubCategory.add(valueAppliance);
		salesDiscountProfitSubCategory.add(valueArt);
		salesDiscountProfitSubCategory.add(valueBinder);
		salesDiscountProfitSubCategory.add(valueChair);
		salesDiscountProfitSubCategory.add(valueCopier);
		salesDiscountProfitSubCategory.add(valueEnvelope);
		salesDiscountProfitSubCategory.add(valueFastener);
		salesDiscountProfitSubCategory.add(valueLabels);
		salesDiscountProfitSubCategory.add(valueMachines);
		salesDiscountProfitSubCategory.add(valuePaper);
		salesDiscountProfitSubCategory.add(valuePhones);
		salesDiscountProfitSubCategory.add(valueStorage);
		salesDiscountProfitSubCategory.add(valueSupply);
		salesDiscountProfitSubCategory.add(valueTable);

		return salesDiscountProfitSubCategory;
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

	private SalesDiscountProfit getDataSalesDiscountProfit(List<DataSalesDiscountProfit> dataSalesDiscountProfits) {

		List<Double> dataSales = new ArrayList<>();
		List<Double> dataDiscount = new ArrayList<>();
		List<Double> dataProfit = new ArrayList<>();
		for (int i = 0; i < dataSalesDiscountProfits.size(); i++) {
			DataSalesDiscountProfit dataSalesDiscountProfit = dataSalesDiscountProfits.get(i);
			dataSales.add(dataSalesDiscountProfit.getSales());
			dataDiscount.add(dataSalesDiscountProfit.getDiscount());
			dataProfit.add(dataSalesDiscountProfit.getProfit());
		}
		SalesDiscountProfit salesDiscountProfit = new SalesDiscountProfit();
		salesDiscountProfit.setDataDiscount(dataDiscount);
		salesDiscountProfit.setDataProfit(dataProfit);
		salesDiscountProfit.setDataSales(dataSales);

		return salesDiscountProfit;
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

	private DataCategoryProfitSalesDiscount allCountryData1(List<OrderEntity> orders) {

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

		Double zeroValue = (double) 0;
		Double profit = zeroValue;
		Double sales = zeroValue;
		Double discount = zeroValue;

		DataCategory dataCategory = new DataCategory();

		if (dataAccessories.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Accessories");
			dataAccessories.add(dataSalesDiscountProfit);
			dataCategory.setDataAccessories(dataAccessories);
		} else {
			dataCategory.setDataAccessories(dataAccessories);
		}
		if (dataAppliances.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Appliances");
			dataAppliances.add(dataSalesDiscountProfit);
			dataCategory.setDataAppliances(dataAppliances);
		} else {
			dataCategory.setDataAppliances(dataAppliances);
		}
		if (dataArt.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Art");
			dataArt.add(dataSalesDiscountProfit);
			dataCategory.setDataArt(dataArt);
		} else {
			dataCategory.setDataArt(dataArt);
		}
		if (dataBinders.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Binders");
			dataBinders.add(dataSalesDiscountProfit);
			dataCategory.setDataBinders(dataBinders);
		} else {
			dataCategory.setDataBinders(dataBinders);
		}
		if (dataBookcases.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Bookcases");
			dataBookcases.add(dataSalesDiscountProfit);
			dataCategory.setDataBookcases(dataBookcases);
		} else {
			dataCategory.setDataBookcases(dataBookcases);
		}
		if (dataChairs.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Chairs");
			dataChairs.add(dataSalesDiscountProfit);
			dataCategory.setDataChairs(dataChairs);
		} else {
			dataCategory.setDataChairs(dataChairs);
		}
		if (dataCopiers.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Copiers");
			dataCopiers.add(dataSalesDiscountProfit);
			dataCategory.setDataCopiers(dataCopiers);
		} else {
			dataCategory.setDataCopiers(dataCopiers);
		}
		if (dataEnvelopes.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Envelopes");
			dataEnvelopes.add(dataSalesDiscountProfit);
			dataCategory.setDataEnvelopes(dataEnvelopes);
		} else {
			dataCategory.setDataEnvelopes(dataEnvelopes);
		}
		if (dataFasteners.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Fasteners");
			dataFasteners.add(dataSalesDiscountProfit);
			dataCategory.setDataFasteners(dataFasteners);
		} else {
			dataCategory.setDataFasteners(dataFasteners);
		}

		if (dataFurnishings.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Furnishings");
			dataFurnishings.add(dataSalesDiscountProfit);
			dataCategory.setDataFurnishings(dataFurnishings);
		} else {
			dataCategory.setDataFurnishings(dataFurnishings);
		}
		if (dataLabels.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Labels");
			dataLabels.add(dataSalesDiscountProfit);
			dataCategory.setDataLabels(dataLabels);
		} else {
			dataCategory.setDataLabels(dataLabels);
		}
		if (dataMachines.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Machines");
			dataMachines.add(dataSalesDiscountProfit);
			dataCategory.setDataMachines(dataMachines);
		} else {
			dataCategory.setDataMachines(dataMachines);
		}
		if (dataPaper.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Paper");
			dataPaper.add(dataSalesDiscountProfit);
			dataCategory.setDataPaper(dataPaper);
		} else {
			dataCategory.setDataPaper(dataPaper);
		}
		if (dataPhones.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Phones");
			dataPhones.add(dataSalesDiscountProfit);
			dataCategory.setDataPhones(dataPhones);
		} else {
			dataCategory.setDataPhones(dataPhones);
		}
		if (dataStorage.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Storage");
			dataStorage.add(dataSalesDiscountProfit);
			dataCategory.setDataStorage(dataStorage);
		} else {
			dataCategory.setDataStorage(dataStorage);
		}
		if (dataSupplies.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Supplies");
			dataSupplies.add(dataSalesDiscountProfit);
			dataCategory.setDataSupplies(dataSupplies);
		} else {
			dataCategory.setDataSupplies(dataSupplies);
		}
		if (dataTables.isEmpty()) {
			DataSalesDiscountProfit dataSalesDiscountProfit = new DataSalesDiscountProfit();
			dataSalesDiscountProfit.setDiscount(discount);
			dataSalesDiscountProfit.setProfit(profit);
			dataSalesDiscountProfit.setSales(sales);
			dataSalesDiscountProfit.setSubCategory("Tables");
			dataTables.add(dataSalesDiscountProfit);
			dataCategory.setDataTables(dataTables);
		} else {
			dataCategory.setDataTables(dataTables);
		}

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