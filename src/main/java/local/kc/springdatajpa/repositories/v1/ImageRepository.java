package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query("SELECT new Image(i.id, i.src) FROM Image i WHERE i.book.id = ?1")
    List<Image> findByBookId(int bookId);
}
