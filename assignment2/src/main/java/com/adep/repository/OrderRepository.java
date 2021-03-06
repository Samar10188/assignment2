package com.adep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adep.entity.OrderEntity;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

	Optional<OrderEntity> findById(Long orderId);

//	@Query(value = "select * from orders o where IFNULL(o.region, '') regexp?1 ", nativeQuery = true)
	@Cacheable("getProfitByRegion")
	List<OrderEntity> findByRegion(String region);

	@Cacheable("salesProfitDiscountValueCountry")
	@Query(value = "select * from orders o where IFNULL(o.country, '') regexp?1 ", nativeQuery = true)
	List<OrderEntity> findByCountry(String country);

	@Cacheable("salesProfitDiscountValue")
	@Query(value = "select * from orders o where IFNULL(o.order_date, '') regexp?1 and IFNULL(o.category, '') regexp?2 and IFNULL(o.sub_category, '') regexp?3 and IFNULL(o.region, '') regexp?4 and IFNULL(o.segment, '') regexp?5", nativeQuery = true)
	List<OrderEntity> findByYear(String year, String category, String subCategory, String region, String segment);

	@Cacheable("findRetunedOrder")
	@Query(value = "SELECT country,sum(sales) FROM Orders INNER JOIN returns ON Orders.Order_ID=returns.Order_ID group by country;", nativeQuery = true)
	List<Object[]> findRetunedOrder();

	@Cacheable("findCounrtySales")
	@Query(value = "SELECT country,sum(sales) FROM Orders group by country;", nativeQuery = true)
	List<Object[]> findCounrtySales();

}
