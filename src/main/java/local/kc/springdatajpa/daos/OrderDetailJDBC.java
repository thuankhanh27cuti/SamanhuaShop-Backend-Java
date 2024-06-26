package local.kc.springdatajpa.daos;

import local.kc.springdatajpa.models.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDetailJDBC {
    private final JdbcTemplate jdbcTemplate;
    private final OptionJDBC optionJDBC;

    @Autowired
    public OrderDetailJDBC(JdbcTemplate jdbcTemplate, OptionJDBC optionJDBC) {
        this.jdbcTemplate = jdbcTemplate;
        this.optionJDBC = optionJDBC;
    }

    public List<OrderDetail> findByOrderId(int orderId) {
        String sql = "SELECT order_total_price, option_quantity, option_id, order_id FROM order_detail WHERE order_id = ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> OrderDetail.builder()
                .price(rs.getFloat("order_total_price"))
                .quantity(rs.getInt("option_quantity"))
                .option(optionJDBC.findById(rs.getInt("option_id")).orElse(null))
                .build()), orderId);
    }
}
