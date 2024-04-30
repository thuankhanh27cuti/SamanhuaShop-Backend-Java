package local.kc.springdatajpa;

import local.kc.springdatajpa.repositories.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

}
