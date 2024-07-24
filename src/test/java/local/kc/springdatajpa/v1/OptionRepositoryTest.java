package local.kc.springdatajpa.v1;

import local.kc.springdatajpa.models.Option;
import local.kc.springdatajpa.repositories.v1.OptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Test
    void findByOrdersDetails_Order_Id() {
        List<Option> options = optionRepository.findByOrdersDetails_Order_Id(25);
        options.forEach(System.out::println);
    }
}
