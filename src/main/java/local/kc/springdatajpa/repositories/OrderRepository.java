package local.kc.springdatajpa.repositories;

import local.kc.springdatajpa.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderDetails od WHERE o.customer.id = ?1")
    List<Order> findByCustomerId(int customerId);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderDetails od WHERE o.id = ?1")
    Optional<Order> findByIdLazy(int id);

    @Query("SELECT new Order (o.id, o.consigneeName, o.address, o.phone, o.createAt, o.finishedAt, o.orderStatus) FROM Order o WHERE o.customer.id = ?1")
    List<Order> findByCustomerIdLazy(int customerId);
}