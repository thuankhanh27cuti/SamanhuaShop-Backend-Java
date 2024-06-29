package local.kc.springdatajpa.v1;

import local.kc.springdatajpa.repositories.v1.WardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WardRepositoryTest {
    @Autowired
    private WardRepository wardRepository;

    @Test
    void findByOrderId() {
        wardRepository.findByOrderId(1).ifPresent(System.out::println);
    }
}
