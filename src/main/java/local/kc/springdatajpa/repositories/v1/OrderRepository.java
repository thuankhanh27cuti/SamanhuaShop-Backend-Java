package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.Order;
import local.kc.springdatajpa.models.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT new Order (o.id, o.consigneeName, o.address, o.phone, o.createAt, o.finishedAt, o.orderStatus) FROM Order o")
    List<Order> findAllLazy(Pageable pageable);

    @Query("SELECT new Order (o.id, o.consigneeName, o.address, o.phone, o.createAt, o.finishedAt, o.orderStatus) FROM Order o WHERE o.orderStatus = ?1")
    List<Order> findByOrderStatusLazy(OrderStatus orderStatus, Pageable pageable);

    @Query("SELECT new Order (o.id, o.consigneeName, o.address, o.phone, o.createAt, o.finishedAt, o.orderStatus) FROM Order o WHERE o.customer.id = ?1")
    List<Order> findByCustomerIdLazy(int customerId, Pageable pageable);

    @Query("SELECT new Order (o.id, o.consigneeName, o.address, o.phone, o.createAt, o.finishedAt, o.orderStatus) FROM Order o WHERE o.customer.id = ?1 AND o.orderStatus = ?2")
    List<Order> findByCustomerIdLazyAndStatus(int customerId, OrderStatus status, Pageable pageable);

    @Query("SELECT new Order (o.id, o.consigneeName, o.address, o.phone, o.createAt, o.finishedAt, o.orderStatus) FROM Order o WHERE o.customer.username = ?1")
    List<Order> findByUsernameLazy(String username, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = ?1")
    long countByStatus(OrderStatus status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.id = ?1")
    long countByCustomerId(int customerId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.id = ?1 AND o.orderStatus = ?2")
    long countByCustomerIdAndStatus(int customerId, OrderStatus status);
}