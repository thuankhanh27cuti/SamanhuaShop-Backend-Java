package local.kc.springdatajpa.repositories;

import local.kc.springdatajpa.models.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {

    @Query("SELECT new Option(o.id, o.name, o.quantity, o.image) FROM Option o INNER JOIN o.book b WHERE b.id = ?1")
    List<Option> findByBookId(int id);

    @Query("SELECT o FROM Option o LEFT JOIN FETCH o.book b WHERE o.id = ?1")
    Optional<Option> findWithBookById(int id);
}
