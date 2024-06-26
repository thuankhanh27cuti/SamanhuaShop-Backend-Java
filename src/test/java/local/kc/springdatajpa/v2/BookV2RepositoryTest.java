package local.kc.springdatajpa.v2;

import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.repositories.v2.BookV2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BookV2RepositoryTest {

    @Autowired
    private BookV2Repository bookV2Repository;

    @Test
    void findAllLazy() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> allLazy = bookV2Repository.findAllLazy(pageable);
        System.out.println(allLazy.size());
    }

    @Test
    void count() {
        long count = bookV2Repository.count();
        System.out.println(count);
    }

    @Test
    void countByCategoryId() {
        long countByCategoryId = bookV2Repository.countByCategoryId(1);
        System.out.println(countByCategoryId);
    }

    @Test
    void findByOptionId() {
        Optional<Book> book = bookV2Repository.findByOptionId(2);
        book.ifPresent(System.out::println);
    }
}
