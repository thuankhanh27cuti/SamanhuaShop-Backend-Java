package local.kc.springdatajpa.v2;

import local.kc.springdatajpa.models.Option;
import local.kc.springdatajpa.repositories.v2.OptionV2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OptionV2RepositoryTest {
    @Autowired
    private OptionV2Repository optionV2Repository;

    @Test
    public void findByBookId() {
        List<Option> options = optionV2Repository.findByBookId(1);
        System.out.println(options);
    }
}
