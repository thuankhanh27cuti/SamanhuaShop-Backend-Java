package local.kc.springdatajpa;

import local.kc.springdatajpa.models.OrderDetail;
import local.kc.springdatajpa.repositories.OrderDetailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    void findByOrderId() {
        Set<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(1);
        orderDetails.forEach(orderDetail -> {
            System.out.println(orderDetail.getOrder());
            System.out.println(orderDetail.getOption());
            System.out.println(orderDetail.getOption().getBook());
        });
    }
    @Test
    void countByOrderId() {
        long l = orderDetailRepository.countByOrderId(1);
        System.out.println(l);
    }
}
