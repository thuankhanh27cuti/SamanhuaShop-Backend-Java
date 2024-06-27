package local.kc.springdatajpa.v2;

import local.kc.springdatajpa.repositories.v2.OrderV2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderV2RepositoryTest {

    @Autowired
    private OrderV2Repository orderV2Repository;

    @Test
    void findTotalPriceById() {
        long totalPriceById = orderV2Repository.findTotalPriceById(1);
        System.out.println(totalPriceById);
    }
}
