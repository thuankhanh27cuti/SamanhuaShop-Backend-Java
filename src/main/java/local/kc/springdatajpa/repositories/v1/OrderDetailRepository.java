package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.OrderDetail;
import local.kc.springdatajpa.models.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    @Query("SELECT new OrderDetail(o.order, o.option, o.quantity, o.price) FROM OrderDetail o WHERE o.order.id = ?1")
    Set<OrderDetail> findByOrderId(Integer orderId);

    @Query("SELECT COUNT(o) FROM OrderDetail o WHERE o.order.id = ?1")
    long countByOrderId(Integer id);

    @Query("SELECT COALESCE(SUM(od.price), 0) FROM OrderDetail od WHERE od.order.customer.id = ?1 AND od.order.orderStatus = 4")
    long getRevenuesOfCustomerById(int id);
}