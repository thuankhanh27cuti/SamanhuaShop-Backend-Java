package local.kc.springdatajpa.repositories;

import local.kc.springdatajpa.models.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    @Query("SELECT new Province(p.code, p.name, p.fullName) FROM Province p")
    List<Province> findAllLazy();

}
