package local.kc.springdatajpa;

import local.kc.springdatajpa.models.Option;
import local.kc.springdatajpa.repositories.OptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Test
    void findByIdLazy() {
        optionRepository.findByIdLazy(1).ifPresent(System.out::println);
    }

    @Test
    void countByBookId() {
        long l = optionRepository.countByBookId(1);
        System.out.println(l);
    }

    @Test
    void findByBookId() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Option> options = optionRepository.findByBookId(1, pageable);
        options.forEach(System.out::println);
    }

    @Test
    void findAllByOptionId() {
        List<Option> options = optionRepository.findAllByBookId(1);
        options.forEach(System.out::println);
    }
}
