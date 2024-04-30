package local.kc.springdatajpa;

import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findEagerById() {
        System.out.println();

        Customer customer = customerRepository.findEagerById(1).orElse(new Customer());
        System.out.println(customer);
        System.out.println();

        Set<Order> orders = customer.getOrders();
        orders.forEach(System.out::println);
        System.out.println();

        orders.forEach(order -> {
            Set<OrderDetail> orderDetails = order.getOrderDetails();
            orderDetails.forEach(orderDetail -> {
                System.out.println(orderDetail);
                Option option = orderDetail.getOption();
                System.out.println(option);
                Book book = option.getBook();
                System.out.println(book);
                System.out.println();
            });
        });
    }

}
