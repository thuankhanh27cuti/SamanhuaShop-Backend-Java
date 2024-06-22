package local.kc.springdatajpa;

import local.kc.springdatajpa.models.Order;
import local.kc.springdatajpa.models.OrderStatus;
import local.kc.springdatajpa.repositories.OrderRepository;
import local.kc.springdatajpa.utils.RevenueByHour;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findAllLazy() {
        List<Order> allLazy = orderRepository.findAllLazy(PageRequest.of(0, 10));
        allLazy.forEach(order -> System.out.println(order.getCustomer()));
    }

    @Test
    void findByCustomerId() {
        int customerId = 1;
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orders = orderRepository.findByCustomerIdLazy(customerId, pageable);
        orders.forEach(System.out::println);
    }

    @Test
    void findByUsernameLazy() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orders = orderRepository.findByUsernameLazy("owner", pageable);
        orders.forEach(System.out::println);
    }

    @Test
    void countByCustomerId() {
        long l = orderRepository.countByCustomerId(2);
        System.out.println(l);
    }

    @Test
    void countByCustomerIdAndStatus() {
        long l = orderRepository.countByCustomerIdAndStatus(1, OrderStatus.SHIPPING);
        System.out.println(l);
    }

    @Test
    void findByFinishedAtMonth() {
        List<Order> orders = orderRepository.findByFinishedAtMonth(4, 2024);
        orders.forEach(System.out::println);
    }

    @Test
    void findByFinishedAt() {
        LocalDate parse = LocalDate.parse("2024-04-26");
        LocalDate localDate = LocalDate.of(2024, 4, 26);
        System.out.println(localDate);
        List<Order> orders = orderRepository.findByFinishedAt(parse);
        orders.forEach(System.out::println);
    }

    @Test
    void sumTotalPriceByHour() {
        LocalDate localDate = LocalDate.of(2024, 4, 25);
        List<RevenueByHour> revenues = orderRepository.sumTotalPriceByHour(localDate);
        revenues.forEach(System.out::println);
    }
}
