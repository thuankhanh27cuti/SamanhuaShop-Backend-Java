package local.kc.springdatajpa.repositories;

import local.kc.springdatajpa.models.Customer;
import local.kc.springdatajpa.models.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT new Customer(c.id, c.name, c.gender, c.image, c.phone, c.username, c.role) FROM Customer c")
    List<Customer> findAllLazy(Pageable pageable);

    @Query("SELECT new Customer(c.id, c.name, c.gender, c.image, c.phone, c.username, c.role) FROM Customer c WHERE c.id = ?1")
    Optional<Customer> findLazyById(Integer id);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.orders o LEFT JOIN FETCH o.orderDetails od WHERE c.id = ?1")
    Optional<Customer> findEagerById(Integer id);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.orders o LEFT JOIN FETCH o.orderDetails od WHERE c.username = ?1")
    Optional<Customer> findEagerByUsername(String username);

    @Query("SELECT c FROM Customer c WHERE c.username = ?1")
    Optional<Customer> findCustomerByUsername(String username);

    @Query("SELECT new Customer(c.id, c.name, c.gender, c.image, c.phone, c.username, c.role) FROM Customer c WHERE c.username = ?1")
    Optional<Customer> findCustomerByUsernameLazy(String username);

    @Query("SELECT new Customer(c.id, c.name, c.gender, c.image, c.phone, c.username, c.role) FROM Customer c WHERE c.role = ?1")
    List<Customer> findByRoles(Role role, Pageable pageable);

    @Query("select COUNT(c) from Customer c where c.role = ?1")
    long countByRole(Role role);
}
