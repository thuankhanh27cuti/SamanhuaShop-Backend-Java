package local.kc.springdatajpa.repositories;

import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.utils.BookStatus;
import local.kc.springdatajpa.utils.RevenueByDate;
import local.kc.springdatajpa.utils.TopSellerBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenericRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenericRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TopSellerBook> getTopSeller(Pageable pageable) {
        Sort sort = pageable.getSort();

        StringBuilder stringBuilder = new StringBuilder("SELECT b.book_id AS id, b.book_name AS name, b.book_image AS image, SUM(od.option_quantity) AS quantity, SUM(od.option_quantity) * b.book_price AS revenue FROM order_detail od LEFT JOIN options o on o.option_id = od.option_id LEFT JOIN books b on b.book_id = o.book_id GROUP BY id");
        if (sort.isSorted()) {
            stringBuilder.append(" ORDER BY ");
            sort.forEach(order -> {
                String property = order.getProperty();
                String direction = order.getDirection().name();
                stringBuilder
                        .append(property)
                        .append(" ")
                        .append(direction)
                        .append(", ");
            });
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append(" LIMIT 5");
        return jdbcTemplate.query(stringBuilder.toString(), (rs, rowNum) -> new TopSellerBook(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("image"),
                rs.getLong("quantity"),
                rs.getLong("revenue")
        ));
    }

    public List<BookStatus> getBookStatus() {
        String sql = """
                SELECT books.book_id AS id, book_name AS name, option_name, option_quantity AS quantity, option_id FROM books
                    LEFT JOIN options o on books.book_id = o.book_id
                    ORDER BY option_quantity
                LIMIT 5;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new BookStatus(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("option_id"),
                rs.getString("option_name"),
                rs.getInt("quantity")
        ));
    }

    public List<Book> getTodayFeatured() {
        String sql = """
                SELECT b.book_id AS id, b.book_name AS name, b.book_image AS image, b.book_price AS price FROM books b
                    LEFT JOIN options o on b.book_id = o.book_id
                    INNER JOIN order_detail od on o.option_id = od.option_id
                GROUP BY id
                ORDER BY SUM(od.option_quantity) DESC
                LIMIT 5
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> Book.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .image(rs.getString("image"))
                .price(rs.getInt("price"))
                .build());
    }

    public List<RevenueByDate> getRevenueByWeek() {
        String sql = """
                SELECT CAST(order_created_at AS DATE ) AS date, SUM(order_total_price) AS revenue FROM orders
                   LEFT JOIN samanhua_shop_jpa_v2_test.order_detail od on orders.order_id = od.order_id
                WHERE CAST(order_created_at AS DATE ) BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY ) AND CURRENT_DATE
                GROUP BY date;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> RevenueByDate.builder()
                .revenue(rs.getInt("revenue"))
                .date(rs.getDate("date"))
                .build());
    }

    public List<String> getAllRoles() {
        String sql = """
                SELECT DISTINCT customer_role FROM customers;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("customer_role"));
    }
}
