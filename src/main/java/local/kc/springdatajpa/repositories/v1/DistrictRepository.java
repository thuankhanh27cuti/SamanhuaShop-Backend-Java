package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query("SELECT new District(d.code, d.name, d.fullName) FROM District d WHERE d.province.code = ?1")
    List<District> findByProvinceId(int id);

    @Query("SELECT new District(d.code, d.name, d.fullName) FROM District d WHERE d.code = ?1")
    Optional<District> findByIdLazy(int id);

    @Query("SELECT new District(d.code, d.name, d.fullName) FROM District d LEFT JOIN d.wards w WHERE w.code = ?1")
    Optional<District> findByWardId(int id);

    @Query("SELECT new District(d.code, d.name, d.fullName) FROM District d LEFT JOIN d.orders o WHERE o.id = ?1")
    Optional<District> findByOrderId(int id);
}
