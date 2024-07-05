package local.kc.springdatajpa.v2;

import local.kc.springdatajpa.models.OrderStatus;
import local.kc.springdatajpa.repositories.v2.OrderV2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderV2Repository orderRepository;

    @Test
    void countByCustomerId() {
        long countByCustomerId = orderRepository.countByCustomerId(1);
        System.out.println(countByCustomerId);
    }

    @Test
    void countByCustomerIdAndOrderStatus() {
        long countByCustomerIdAndOrderStatus = orderRepository.countByCustomerIdAndOrderStatus(1, OrderStatus.SUCCESS);
        System.out.println(countByCustomerIdAndOrderStatus);
    }

}
