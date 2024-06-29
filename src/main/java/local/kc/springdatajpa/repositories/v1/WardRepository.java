package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    @Query("SELECT new Ward(w.code, w.name, w.fullName) FROM Ward w WHERE w.district.code = ?1")
    List<Ward> findByDistrictId(int districtId);

    @Query("SELECT new Ward(w.code, w.name, w.fullName) FROM Ward w WHERE w.code = ?1")
    Optional<Ward> findByIdLazy(int id);

    @Query("SELECT new Ward(w.code, w.name, w.fullName) FROM Ward w LEFT JOIN w.customers c WHERE c.id = ?1")
    Optional<Ward> findByCustomerId(int id);

    @Query("SELECT new Ward(w.code, w.name, w.fullName) FROM Ward w LEFT JOIN w.orders o WHERE o.id = ?1")
    Optional<Ward> findByOrderId(int id);
}
