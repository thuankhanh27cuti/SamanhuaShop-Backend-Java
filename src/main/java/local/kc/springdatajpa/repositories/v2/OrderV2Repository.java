package local.kc.springdatajpa.repositories.v2;

import local.kc.springdatajpa.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderV2Repository extends JpaRepository<Order, Integer> {

    @Query("select SUM(od.price) from Order o LEFT JOIN o.orderDetails od where o.id = ?1")
    long findTotalPriceById(Integer orderId);
}
