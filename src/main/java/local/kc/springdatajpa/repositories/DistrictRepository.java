package local.kc.springdatajpa.repositories;

import local.kc.springdatajpa.models.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query("SELECT new District(d.code, d.name, d.fullName) FROM District d WHERE d.province.code = ?1")
    List<District> findByProvinceId(int id);
}
