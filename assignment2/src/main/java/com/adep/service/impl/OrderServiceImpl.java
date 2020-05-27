package com.adep.service.impl;

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
import com.adep.shared.model.DataCategory;
import com.adep.shared.model.DataSalesDiscountProfit;
import com.adep.shared.model.DataValueNameColor;
import com.adep.shared.model.SalesProfitDiscountValue;
import com.adep.shared.model.SeriesData;

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
		DataCategory dataCategory = allCountryData(list);

		List<DataSalesDiscountProfit> dataSalesDiscountProfit = new ArrayList<>();
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataAccessories()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataAppliances()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataArt()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataBinders()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataChairs()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCopiers()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataEnvelopes()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataFasteners()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataFurnishings()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataLabels()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataMachines()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataPaper()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataPhones()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataStorage()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataSupplies()));
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataTables()));

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
	public SalesProfitDiscountValue getSalesProfitDiscountValueByCountry(String country) {
		if (country.contains("world")) {
			country = ".*";
		}

		List<OrderEntity> list = new ArrayList<>();
		Iterable<OrderEntity> orderAll = orderRepository.findByCountry(country);
		for (OrderEntity orderEntity : orderAll) {
			list.add(orderEntity);
		}
		DataCategory dataCategory = allCountryData(list);

		List<DataSalesDiscountProfit> dataSalesDiscountProfit = new ArrayList<>();
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataAccessories()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataAppliances()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataArt()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataBookcases()));
		}
		dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataBinders()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataChairs()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataCopiers()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataEnvelopes()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataFasteners()));

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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataFurnishings()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataLabels()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataMachines()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataPaper()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataPhones()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataStorage()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataSupplies()));
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
		} else {
			dataSalesDiscountProfit.add(dataSalesProfitDiscount(dataCategory.getDataTables()));
		}
		List<DataValueNameColor> salesData = getSalesDataValueNameColor(dataSalesDiscountProfit);
		List<DataValueNameColor> profitData = getProfitDataValueNameColor(dataSalesDiscountProfit);
		List<DataValueNameColor> discountData = getDiscountDataValueNameColor(dataSalesDiscountProfit);

		SalesProfitDiscountValue salesProfitDiscountValue = new SalesProfitDiscountValue();
		salesProfitDiscountValue.setSalesData(salesData);
		salesProfitDiscountValue.setDiscountData(discountData);
		salesProfitDiscountValue.setProfitData(profitData);

		return salesProfitDiscountValue;
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

	private DataCategory allCountryData(List<OrderEntity> orders) {

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

		return dataCategory;
	}

	private DataSalesDiscountProfit dataSalesProfitDiscount(List<DataSalesDiscountProfit> dataSubCategory) {

		Double profit = (double) 0;
		Double sales = (double) 0;
		Double discount = (double) 0;
		Double avgDiscount = (double) 0;

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

}