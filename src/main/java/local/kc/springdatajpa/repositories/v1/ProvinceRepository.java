package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    @Query("SELECT new Province(p.code, p.name, p.fullName) FROM Province p")
    List<Province> findAllLazy();

    @Query("SELECT new Province(p.code, p.name, p.fullName) FROM Province p WHERE p.code = ?1")
    Optional<Province> findByIdLazy(int id);

    @Query("SELECT new Province(p.code, p.name, p.fullName) FROM Province p LEFT JOIN p.districts d WHERE d.code = ?1")
    Optional<Province> findByDistrictId(int id);

    @Query("SELECT new Province(p.code, p.name, p.fullName) FROM Province p LEFT JOIN p.orders o WHERE o.id = ?1")
    Optional<Province> findByOrderId(int id);
}
