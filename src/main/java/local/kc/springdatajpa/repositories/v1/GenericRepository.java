package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.converters.RoleConverter;
import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.utils.*;
import local.kc.springdatajpa.utils.RevenueByHour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GenericRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RoleConverter roleConverter;


    @Autowired
    public GenericRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.roleConverter = new RoleConverter();
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

    public List<RevenueByDate> getRevenueByWeek() {
        String sql = """
                SELECT CAST(order_created_at AS DATE ) AS date, SUM(order_total_price) AS revenue FROM orders
                   LEFT JOIN order_detail od on orders.order_id = od.order_id
                WHERE CAST(order_created_at AS DATE ) BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY ) AND CURRENT_DATE
                GROUP BY date;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> RevenueByDate.builder()
                .revenue(rs.getInt("revenue"))
                .date(rs.getDate("date"))
                .build());
    }

    public List<CustomerStatistical> getCustomerStatistical(Pageable pageable) {
        return getCustomerStatistical(null, pageable);
    }

    public List<CustomerStatistical> getCustomerStatistical(Role role, Pageable pageable) {
        List<Object> args = new ArrayList<>();
        String sql = """
                SELECT c.customer_id AS customer_id,
                       customer_gender AS gender,
                       customer_image AS image,
                       customer_full_name AS full_name,
                       customer_phone AS phone,
                       customer_role AS role,
                       customer_username AS username,
                       COUNT(IF(order_status = 1, o.order_id, null)) AS order_remain,
                       COUNT(IF(order_status = 3, o.order_id, null)) AS order_shipping,
                       COUNT(IF(order_status = 4, o.order_id, null)) AS order_success,
                       COUNT(order_id) AS count_order,
                       MAX(IF(order_status = 1, order_created_at, null)) AS last_pending,
                       (SELECT SUM(od2.order_total_price)
                        FROM customers c2
                                 LEFT JOIN orders o2 ON c2.customer_id = o2.customer_id
                                 LEFT JOIN order_detail od2 ON o2.order_id = od2.order_id
                        WHERE c2.customer_id = c.customer_id
                        GROUP BY c2.customer_id) AS total_price
                FROM customers c
                        LEFT JOIN orders o on c.customer_id = o.customer_id
        """;
        if (role != null) {
            sql = sql + """
                WHERE customer_role = ?
                """;
            args.add(role.getValue());
        }
        sql = sql + """
                GROUP BY c.customer_id
                """;
        Sort sort = pageable.getSort();
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int start = pageNumber * pageSize;
        String build = new QueryBuilder.Builder()
                .select(sql)
                .sorted(sort)
                .limit(start, pageSize)
                .build();
        return jdbcTemplate.query(build, (rs, rowNum) -> CustomerStatistical.builder()
                .id(rs.getInt("customer_id"))
                .gender(rs.getString("gender"))
                .image(rs.getString("image"))
                .fullName(rs.getString("full_name"))
                .phone(rs.getString("phone"))
                .role(roleConverter.convertToEntityAttribute(rs.getInt("role")))
                .username(rs.getString("username"))
                .orderRemain(rs.getInt("order_remain"))
                .orderShipping(rs.getInt("order_shipping"))
                .orderSuccess(rs.getInt("order_success"))
                .countOrder(rs.getInt("count_order"))
                .lastPending(rs.getTimestamp("last_pending"))
                .totalPrice(rs.getFloat("total_price"))
                .build(), args.toArray());
    }

    public List<RevenueByDate> getRevenueByMonth(int month, int year) {
        String sql = """
                SELECT CAST(order_created_at AS DATE ) as date , SUM(order_total_price) as totalPrice FROM orders o
                    LEFT JOIN order_detail od on o.order_id = od.order_id
                WHERE MONTH(order_created_at) = ? AND YEAR(order_created_at) = ?
                GROUP BY date;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new RevenueByDate(rs.getDate("date"), rs.getInt("totalPrice")), month, year);
    }

    public List<RevenueByHour> getRevenueByDate(LocalDate date) {
        String sql = """
            SELECT HOUR(order_created_at) as hour, SUM(order_total_price) as revenue FROM orders o
            LEFT JOIN order_detail od on o.order_id = od.order_id
            WHERE CAST(order_created_at as DATE) = ?
            GROUP BY HOUR(order_created_at);
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> RevenueByHour.builder()
                .hour(rs.getInt("hour"))
                .revenue(rs.getInt("revenue"))
                .build(), date.toString());
    }

    public List<RevenueByMonth> getRevenueByYear(int year) {
        String sql = """
        SELECT MONTH(order_created_at) as month, YEAR(order_created_at) as year, SUM(order_total_price) as totalPrice
        FROM orders
            LEFT JOIN order_detail od on orders.order_id = od.order_id
        WHERE YEAR(order_created_at) = ?
        GROUP BY month, year;
        """;
        return jdbcTemplate.query(sql, ((rs, rowNum) -> RevenueByMonth.builder()
                .month(rs.getInt("month"))
                .year(rs.getInt("year"))
                .revenue(rs.getInt("totalPrice"))
                .build()), year);
    }

    public List<RevenueByYear> getRevenueAllTime() {
        String sql = """
            SELECT YEAR(order_created_at) as year, SUM(order_total_price) as revenue FROM orders
            LEFT JOIN order_detail od on orders.order_id = od.order_id
            GROUP BY year;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> RevenueByYear.builder()
                .year(rs.getInt("year"))
                .revenue(rs.getInt("revenue"))
                .build());
    }
}
