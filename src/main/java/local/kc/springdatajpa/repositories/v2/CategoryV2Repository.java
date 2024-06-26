package local.kc.springdatajpa.repositories.v2;

import local.kc.springdatajpa.models.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryV2Repository extends JpaRepository<Category, Integer> {

    @Query("SELECT new Category(c.id, c.name, c.image) FROM Category c WHERE c.isDeleted = false")
    List<Category> findAllLazy(Pageable pageable);

    @Query("SELECT new Category(c.id, c.name, c.image) FROM Category c WHERE c.isDeleted = false")
    List<Category> findAllLazy();

    @Query("SELECT new Category(c.id, c.name, c.image) FROM Category c LEFT JOIN c.books b WHERE b.id = ?1 AND c.isDeleted = false")
    List<Category> findByBookIdLazy(int bookId);
}
