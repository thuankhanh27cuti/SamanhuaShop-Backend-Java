package local.kc.springdatajpa.repositories.v2;

import local.kc.springdatajpa.models.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookV2Repository extends JpaRepository<Book, Integer> {

    @Query("SELECT new Book (b.id, b.name, b.image, b.price, b.description, b.createAt) FROM Book b WHERE b.isDeleted = false")
    List<Book> findAllLazy(Pageable pageable);

    @Query("SELECT new Book(b.id, b.name, b.image, b.price, b.description, b.createAt) FROM Book b LEFT JOIN b.categories c WHERE b.isDeleted = false and c.id = ?1")
    List<Book> findByCategoryIdLazy(int categoryId, Pageable pageable);

    @Query("SELECT new Book (b.id, b.name, b.image, b.price, b.description, b.createAt) FROM Book b WHERE b.id = ?1 AND b.isDeleted = false")
    Optional<Book> findByIdLazy(int id);

    @Query("SELECT new Book (b.id, b.name, b.image, b.price, b.description, b.createAt) FROM Book b LEFT JOIN b.options o WHERE o.id = ?1 AND o.isDeleted = false AND b.isDeleted = false")
    Optional<Book> findByOptionId(int id);

    @Override
    @Query("SELECT COUNT(b) FROM Book b WHERE b.isDeleted = false")
    long count();

    @Query("SELECT COUNT(b) FROM Category c LEFT JOIN c.books b WHERE c.id = ?1 AND b.isDeleted = false")
    long countByCategoryId(int id);
}
