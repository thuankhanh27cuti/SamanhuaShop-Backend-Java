package local.kc.springdatajpa.daos;

import local.kc.springdatajpa.models.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OptionJDBC {
    private final JdbcTemplate jdbcTemplate;
    private final BookJDBC bookJDBC;

    @Autowired
    public OptionJDBC(JdbcTemplate jdbcTemplate, BookJDBC bookJDBC) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookJDBC = bookJDBC;
    }

    public Optional<Option> findById(int id) {
        String sql = "select option_id, option_image, option_name, option_quantity, book_id from options where option_id = ?";
        List<Option> options = jdbcTemplate.query(sql, (rs, rowNum) -> Option.builder()
                .id(rs.getInt("option_id"))
                .name(rs.getString("option_name"))
                .image(rs.getString("option_image"))
                .quantity(rs.getInt("option_quantity"))
                .book(bookJDBC.findById(rs.getInt("book_id")).orElse(null))
                .build(), id);
        return options.isEmpty() ? Optional.empty() : Optional.of(options.get(0));
    }
}
