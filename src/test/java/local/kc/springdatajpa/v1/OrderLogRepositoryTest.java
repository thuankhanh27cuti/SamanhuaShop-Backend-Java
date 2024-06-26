package local.kc.springdatajpa.v1;

import local.kc.springdatajpa.models.OrderLog;
import local.kc.springdatajpa.repositories.v1.OrderLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OrderLogRepositoryTest {

    @Autowired
    private OrderLogRepository orderLogRepository;

    @Test
    public void findAll() {
        List<OrderLog> logs = orderLogRepository.findByOrderId(3);
        System.out.println(logs);
    }
}
