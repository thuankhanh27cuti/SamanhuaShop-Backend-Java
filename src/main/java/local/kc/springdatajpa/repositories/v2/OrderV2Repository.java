package local.kc.springdatajpa.repositories.v2;

import local.kc.springdatajpa.models.Order;
import local.kc.springdatajpa.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderV2Repository extends JpaRepository<Order, Integer> {
    @Query("SELECT new Order(o.id, o.paymentMethod) FROM Order o WHERE o.id = ?1")
    Optional<Order> findByIdLazy(int id);

    @Query("select SUM(od.price) from Order o LEFT JOIN o.orderDetails od where o.id = ?1")
    long findTotalPriceById(Integer orderId);

    @Query("select count(o) from Order o where o.customer.id = ?1")
    long countByCustomerId(int id);

    @Query("select count(o) from Order o where o.customer.id = ?1 and o.orderStatus = ?2")
    long countByCustomerIdAndOrderStatus(int id, OrderStatus orderStatus);

}
