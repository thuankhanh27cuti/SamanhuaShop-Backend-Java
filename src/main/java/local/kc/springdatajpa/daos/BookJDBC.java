package local.kc.springdatajpa.daos;

import local.kc.springdatajpa.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookJDBC {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Book> findById(int id) {
        String sql = "SELECT book_id, book_image, book_name, book_price FROM books WHERE book_id = ?";
        List<Book> books = jdbcTemplate.query(sql, (rs, rowNum) -> Book.builder()
                .id(rs.getInt("book_id"))
                .image(rs.getString("book_image"))
                .name(rs.getString("book_name"))
                .price(rs.getInt("book_price"))
                .build(), id);
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }
}
