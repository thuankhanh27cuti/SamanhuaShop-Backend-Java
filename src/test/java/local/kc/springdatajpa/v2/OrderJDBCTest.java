package local.kc.springdatajpa.v2;

import local.kc.springdatajpa.daos.OrderJDBC;
import local.kc.springdatajpa.models.Order;
import local.kc.springdatajpa.models.OrderDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class OrderJDBCTest {
    @Autowired
    private OrderJDBC orderJDBC;

    @Test
    void findByCustomerId() {
        List<Order> orders = orderJDBC.findByCustomerId(1);
        orders.forEach(order -> {
            Set<OrderDetail> orderDetails = order.getOrderDetails();
            orderDetails.forEach(orderDetail -> System.out.println(orderDetail.getOption()));
        });
    }
}
