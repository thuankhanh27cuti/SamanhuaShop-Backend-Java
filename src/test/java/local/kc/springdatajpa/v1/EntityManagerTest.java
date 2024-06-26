package local.kc.springdatajpa.v1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import local.kc.springdatajpa.models.Book;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EntityManagerTest {

    @PersistenceContext
    private EntityManager em;

    @Test
    void getBookById() {
        Book book = em.find(Book.class, 1);
        System.out.println(book);
    }
}
