package local.kc.springdatajpa.v1;

import local.kc.springdatajpa.models.OrderDetail;
import local.kc.springdatajpa.repositories.v1.OrderDetailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void getRevenuesOfCustomerById() {
        long revenuesOfCustomerById = orderDetailRepository.getRevenuesOfCustomerById(3);
        System.out.println(revenuesOfCustomerById);
    }

    @Test
    public void findByOrderId() {
        Set<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(25);
        orderDetails.forEach(orderDetail -> {
            System.out.println(orderDetail);
            System.out.println(orderDetail.getOption());
        });
    }
}
