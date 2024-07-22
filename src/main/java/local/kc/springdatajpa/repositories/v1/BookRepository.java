package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT new Book(b.id, b.name, b.image, b.price, b.description, b.createAt) FROM Book b WHERE b.id = ?1")
    Optional<Book> findByIdLazy(int id);

    @Query("SELECT new Book(b.id, b.name, b.image, b.price, b.description, b.createAt, b.isDeleted) FROM Book b")
    List<Book> findAllLazy(Pageable pageable);

    @Query("SELECT new Book(b.id, b.name, b.image, b.price, b.description, b.createAt) FROM Book b LEFT JOIN b.categories c WHERE c.id = ?1")
    List<Book> findByCategoryIdLazy(int categoryId, Pageable pageable);

    @Query("SELECT new Book(b.id, b.name, b.image, b.price, b.description, b.createAt) FROM Book b LEFT JOIN b.options o WHERE o.id = ?1")
    Optional<Book> findByOptionId(int id);

    @Query("SELECT COUNT(b) FROM Book b INNER JOIN b.categories c WHERE c.id = ?1")
    long countByCategoryId(int id);
}
