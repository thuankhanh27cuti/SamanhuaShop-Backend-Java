package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.utils.*;
import local.kc.springdatajpa.utils.chart.ChartByDate;
import local.kc.springdatajpa.utils.chart.ChartByHour;
import local.kc.springdatajpa.utils.chart.ChartByMonth;
import local.kc.springdatajpa.utils.chart.ChartByYear;
import local.kc.springdatajpa.utils.statistical.StatisticalByDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
        String sql = """
                SELECT b.book_id AS id, b.book_name AS name, b.book_image AS image, SUM(od.option_quantity) AS quantity, SUM(od.option_quantity) * b.book_price AS revenue FROM order_detail od LEFT JOIN options o on o.option_id = od.option_id LEFT JOIN books b on b.book_id = o.book_id GROUP BY id
                """;
        String build = new QueryBuilder.Builder()
                .select(sql)
                .sorted(sort)
                .limit(0, 5)
                .build();
        return jdbcTemplate.query(build, (rs, rowNum) -> new TopSellerBook(
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

    public List<ChartByDate> getRevenueByWeek() {
        String sql = """
                SELECT CAST(order_created_at AS DATE ) AS date, SUM(order_total_price) AS revenue FROM orders
                   LEFT JOIN order_detail od on orders.order_id = od.order_id
                WHERE CAST(order_created_at AS DATE ) BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY ) AND CURRENT_DATE
                GROUP BY date;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> ChartByDate.builder()
                .revenue(rs.getInt("revenue"))
                .date(rs.getDate("date"))
                .build());
    }

    public List<ChartByDate> getRevenueByMonth(int month, int year) {
        String sql = """
                SELECT CAST(order_created_at AS DATE ) as date , SUM(order_total_price) as totalPrice FROM orders o
                    LEFT JOIN order_detail od on o.order_id = od.order_id
                WHERE MONTH(order_created_at) = ? AND YEAR(order_created_at) = ?
                GROUP BY date;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ChartByDate(rs.getDate("date"), rs.getInt("totalPrice")), month, year);
    }

    public List<ChartByHour> getRevenueByDate(LocalDate date) {
        String sql = """
            SELECT HOUR(order_created_at) as hour, SUM(order_total_price) as revenue FROM orders o
            LEFT JOIN order_detail od on o.order_id = od.order_id
            WHERE CAST(order_created_at as DATE) = ?
            GROUP BY HOUR(order_created_at);
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> ChartByHour.builder()
                .hour(rs.getInt("hour"))
                .revenue(rs.getInt("revenue"))
                .build(), date.toString());
    }

    public List<ChartByMonth> getRevenueByYear(int year) {
        String sql = """
        SELECT MONTH(order_created_at) as month, YEAR(order_created_at) as year, SUM(order_total_price) as totalPrice
        FROM orders
            LEFT JOIN order_detail od on orders.order_id = od.order_id
        WHERE YEAR(order_created_at) = ?
        GROUP BY month, year;
        """;
        return jdbcTemplate.query(sql, ((rs, rowNum) -> ChartByMonth.builder()
                .month(rs.getInt("month"))
                .year(rs.getInt("year"))
                .revenue(rs.getInt("totalPrice"))
                .build()), year);
    }

    public List<ChartByYear> getRevenueAllTime() {
        String sql = """
            SELECT YEAR(order_created_at) as year, SUM(order_total_price) as revenue FROM orders
            LEFT JOIN order_detail od on orders.order_id = od.order_id
            GROUP BY year;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> ChartByYear.builder()
                .year(rs.getInt("year"))
                .revenue(rs.getInt("revenue"))
                .build());
    }

    public List<StatisticalByDate> getStatisticalRevenueByDate(LocalDate date) {
        String sql = """
        SELECT orders.order_id, orders.order_finished_at, SUM(od.order_total_price) AS sum FROM orders LEFT JOIN order_detail od on orders.order_id = od.order_id WHERE order_status = 4 AND DATE (order_finished_at) = ? GROUP BY orders.order_id;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> StatisticalByDate.builder()
                .id(rs.getInt("order_id"))
                .finishedAt(rs.getTimestamp("order_finished_at"))
                .sum(rs.getLong("sum"))
                .build(), date.toString());
    }

    public List<ChartByDate> getStatisticalRevenueByMonth(int month, int year) {
        String sql = """
            SELECT DATE(order_finished_at) AS date, SUM(od.order_total_price) AS revenue FROM orders LEFT JOIN order_detail od on orders.order_id = od.order_id WHERE order_status = 4 AND MONTH(order_finished_at) = ? AND YEAR(order_finished_at) = ? GROUP BY date;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ChartByDate(rs.getDate("date"), rs.getInt("revenue")), month, year);
    }

    public List<ChartByMonth> getStatisticalRevenueByYear(int year) {
        String sql = """
            SELECT MONTH(order_finished_at) AS month, YEAR(order_finished_at) AS year, SUM(od.order_total_price) AS totalPrice FROM orders LEFT JOIN order_detail od on orders.order_id = od.order_id WHERE YEAR(order_finished_at) = ? AND order_status = 4 GROUP BY month, year;
        """;
        return jdbcTemplate.query(sql, ((rs, rowNum) -> ChartByMonth.builder()
                .month(rs.getInt("month"))
                .year(rs.getInt("year"))
                .revenue(rs.getInt("totalPrice"))
                .build()), year);
    }

    public Object getStatisticalRevenueAllTime() {
        String sql = """
            SELECT YEAR(order_finished_at) as year, SUM(od.order_total_price) as totalPrice FROM orders LEFT JOIN order_detail od on orders.order_id = od.order_id WHERE order_status = 4 GROUP BY year;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> ChartByYear.builder()
                .year(rs.getInt("year"))
                .revenue(rs.getInt("totalPrice"))
                .build());
    }
}
