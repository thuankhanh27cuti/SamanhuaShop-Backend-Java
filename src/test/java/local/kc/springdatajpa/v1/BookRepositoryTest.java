package local.kc.springdatajpa.v1;

import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.repositories.v1.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findByOptionId() {
        Optional<Book> book = bookRepository.findByOptionId(1);
        book.ifPresent(book1 -> System.out.println(book1.getOptions()));
    }

    @Test
    void findByNameLikeIgnoreCase() {
        List<Book> books = bookRepository.findByNameLikeIgnoreCase("%ta%");
        books.forEach(book -> System.out.println(book.getName()));
        System.out.println(books.size());
    }

    @Test
    void findByNameStartsWithIgnoreCase() {
        List<Book> books = bookRepository.findByNameStartsWithIgnoreCase("%ta%");
        books.forEach(book -> System.out.println(book.getName()));
        System.out.println(books.size());

    }
}
