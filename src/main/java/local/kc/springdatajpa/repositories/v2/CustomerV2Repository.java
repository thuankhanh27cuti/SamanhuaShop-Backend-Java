package local.kc.springdatajpa.repositories.v2;

import local.kc.springdatajpa.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerV2Repository extends JpaRepository<Customer, Integer> {

    @Query("SELECT new Customer (c.id, c.name, c.gender, c.birthday, c.image, c.phone) FROM Customer c WHERE c.id = ?1")
    Optional<Customer> findByIdLazy(int id);
}
