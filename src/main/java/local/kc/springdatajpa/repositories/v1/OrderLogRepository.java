package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLogRepository extends JpaRepository<OrderLog, Integer> {

    @Query("SELECT new OrderLog(o.id, o.description, o.time) FROM OrderLog o WHERE o.order.id = ?1 ORDER BY o.id ASC")
    List<OrderLog> findByOrderId(Integer orderId);
}
