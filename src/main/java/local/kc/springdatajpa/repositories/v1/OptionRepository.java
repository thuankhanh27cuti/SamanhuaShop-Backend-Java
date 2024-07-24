package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.Option;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {
    @Query("SELECT new Option(o.id, o.name, o.quantity, o.image) FROM Option o WHERE o.id = ?1")
    Optional<Option> findByIdLazy(int id);

    @Query("SELECT new Option(o.id, o.name, o.quantity, o.image) FROM Option o WHERE o.book.id = ?1")
    List<Option> findAllByBookId(int id);

    @Query("SELECT new Option(o.id, o.name, o.quantity, o.image) FROM Option o WHERE o.book.id = ?1")
    List<Option> findByBookId(int id, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Option o WHERE o.book.id = ?1")
    long countByBookId(Integer id);

    @Query("SELECT new Option(o.id, o.name, o.quantity, o.image) FROM Option o ORDER BY o.quantity ASC")
    List<Option> findAllOptionQuantityAsc(Pageable pageable);

    @Query("SELECT COUNT (o) FROM Option o WHERE o.quantity <= ?1")
    long countByQuantityLessThanEqual(Integer quantity);

    @Query("SELECT o FROM Option o INNER JOIN o.ordersDetails od WHERE od.order.id = ?1")
    List<Option> findByOrdersDetails_Order_Id(Integer id);
}
