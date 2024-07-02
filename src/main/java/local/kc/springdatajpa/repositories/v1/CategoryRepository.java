package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT new Category(c.id, c.name, c.image) FROM Category c WHERE c.id = ?1")
    Optional<Category> findByIdLazy(int id);

    @Query("SELECT new Category(c.id, c.name) FROM Category c")
    List<Category> findAllLazy();

    @Query("SELECT new Category(c.id, c.name, c.image,c.isDeleted) FROM Category c")
    List<Category> findAllLazy(Pageable pageable);

    @Query("SELECT new Category(c.id, c.name, c.image,c.isDeleted) FROM Category c LEFT JOIN c.books b WHERE b.id = ?1")
    List<Category> findByBookIdLazy(int bookId);

}
