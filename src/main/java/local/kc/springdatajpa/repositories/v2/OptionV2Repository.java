package local.kc.springdatajpa.repositories.v2;

import local.kc.springdatajpa.models.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionV2Repository extends JpaRepository<Option, Integer> {
    @Query("SELECT new Option(o.id, o.name, o.quantity, o.image) FROM Option o WHERE o.book.id = ?1 AND o.isDeleted = false AND o.book.isDeleted = false")
    List<Option> findByBookId(int id);

    @Query("SELECT new Option(o.id, o.name, o.quantity, o.image) FROM Option o WHERE o.id = ?1")
    Optional<Option> findByIdLazy(int id);
}
