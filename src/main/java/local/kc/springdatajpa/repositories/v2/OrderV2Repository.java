package local.kc.springdatajpa.repositories.v2;

import local.kc.springdatajpa.models.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderV2Repository extends JpaRepository<Order, Integer> {

    @Query("SELECT new Order (o.id, o.consigneeName, o.address, o.phone, o.createAt, o.finishedAt, o.paymentMethod, o.orderStatus) FROM Order o WHERE o.customer.id = ?1 AND o.customer.isDeleted = false")
    List<Order> findByCustomerIdLazy(int id, Pageable pageable);
}
