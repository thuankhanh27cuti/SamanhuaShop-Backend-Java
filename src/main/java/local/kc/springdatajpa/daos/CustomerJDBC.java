package local.kc.springdatajpa.daos;

import local.kc.springdatajpa.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerJDBC {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Customer> findById(int id) {
        String sql = "SELECT customer_id, customer_username FROM customers WHERE customer_id = ?";
        List<Customer> customers = jdbcTemplate.query(sql, (rs, rowNum) -> Customer.builder()
                .id(rs.getInt("customer_id"))
                .username(rs.getString("customer_username"))
                .build(), id);
        return customers.isEmpty() ? Optional.empty() : Optional.of(customers.get(0));
    }
}
