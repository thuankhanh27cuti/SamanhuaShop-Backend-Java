package local.kc.springdatajpa.repositories.v1;

import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.utils.*;
import local.kc.springdatajpa.utils.chart.*;
import local.kc.springdatajpa.utils.statistical.*;
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
                SELECT CAST(order_finished_at AS DATE ) AS date, SUM(order_total_price) AS revenue FROM orders
                   LEFT JOIN order_detail od on orders.order_id = od.order_id
                WHERE CAST(order_finished_at AS DATE ) BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY ) AND CURRENT_DATE AND order_status = 4
                GROUP BY date;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> ChartByDate.builder()
                .revenue(rs.getInt("revenue"))
                .date(rs.getDate("date"))
                .build());
    }

    public List<ChartByDate> getRevenueByMonth(int month, int year) {
        String sql = """
                SELECT CAST(order_finished_at AS DATE ) as date , SUM(order_total_price) as totalPrice FROM orders o
                    LEFT JOIN order_detail od on o.order_id = od.order_id
                WHERE MONTH(order_finished_at) = ? AND YEAR(order_finished_at) = ? AND order_status = 4
                GROUP BY date;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ChartByDate(rs.getDate("date"), rs.getInt("totalPrice")), month, year);
    }

    public List<ChartByHour> getRevenueByDate(LocalDate date) {
        String sql = """
            SELECT HOUR(order_finished_at) as hour, SUM(order_total_price) as revenue FROM orders o
            LEFT JOIN order_detail od on o.order_id = od.order_id
            WHERE CAST(order_finished_at as DATE) = ? AND order_status = 4
            GROUP BY HOUR(order_finished_at);
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> ChartByHour.builder()
                .hour(rs.getInt("hour"))
                .revenue(rs.getInt("revenue"))
                .build(), date.toString());
    }

    public List<ChartByMonth> getRevenueByYear(int year) {
        String sql = """
        SELECT MONTH(order_finished_at) as month, YEAR(order_finished_at) as year, SUM(order_total_price) as totalPrice
        FROM orders
            LEFT JOIN order_detail od on orders.order_id = od.order_id
        WHERE YEAR(order_finished_at) = ? AND order_status = 4
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
            SELECT YEAR(order_finished_at) as year, SUM(order_total_price) as revenue FROM orders
            LEFT JOIN order_detail od on orders.order_id = od.order_id
            WHERE order_status = 4
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

    public List<ChartByYear> getStatisticalRevenueAllTime() {
        String sql = """
            SELECT YEAR(order_finished_at) as year, SUM(od.order_total_price) as totalPrice FROM orders LEFT JOIN order_detail od on orders.order_id = od.order_id WHERE order_status = 4 GROUP BY year;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> ChartByYear.builder()
                .year(rs.getInt("year"))
                .revenue(rs.getInt("totalPrice"))
                .build());
    }

    public long getCountValuableCustomer() {
        String sql = """
                SELECT SUM(IF(sub_query.total_price > 50000, 1, 0)) as count FROM (
                    SELECT SUM(od.order_total_price) as total_price
                    FROM order_detail od
                            LEFT JOIN orders o on o.order_id = od.order_id
                            LEFT JOIN customers c on c.customer_id = o.customer_id
                    WHERE o.order_status = 4
                    GROUP BY c.customer_id
                ) AS sub_query;
        """;
        List<Long> count = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("count"));
        return count.isEmpty() ? 0 : count.get(0);
    }

    public List<StatisticalByProvince> getStatisticalRevenueByProvince() {
        String sql = """
                SELECT province_code,
                       province_name,
                       province_full_name,
                       COALESCE(o.revenue, 0) AS revenue
                FROM provinces p LEFT JOIN (
                    SELECT orders.province_id,
                           SUM(order_total_price) AS revenue
                    FROM orders
                        LEFT JOIN order_detail od ON orders.order_id = od.order_id
                    WHERE order_status = 4
                    GROUP BY orders.province_id
                ) AS o ON p.province_code = o.province_id;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> StatisticalByProvince.builder()
                .id(rs.getInt("province_code"))
                .name(rs.getString("province_name"))
                .revenue(rs.getInt("revenue"))
                .build());
    }

    public List<StatisticalByDistrict> getStatisticalRevenueByDistrict(int provinceId) {
        String sql = """
                SELECT district_code,
                       district_full_name,
                       district_name,
                       COALESCE(o.revenue, 0) AS revenue
                FROM districts d LEFT JOIN (
                    SELECT orders.district_id,
                           SUM(order_total_price) AS revenue
                    FROM orders
                             LEFT JOIN order_detail od ON orders.order_id = od.order_id
                    WHERE order_status = 4
                    GROUP BY orders.district_id
                ) AS o ON d.district_code = o.district_id WHERE province_id = ?
                ORDER BY revenue DESC, district_code;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> StatisticalByDistrict.builder()
                .id(rs.getInt("district_code"))
                .name(rs.getString("district_name"))
                .revenue(rs.getInt("revenue"))
                .build(), provinceId);
    }

    public List<StatisticalByWard> getStatisticalRevenueByWard(int districtId) {
        String sql = """
                SELECT ward_code, ward_full_name, ward_name, COALESCE(o.revenue, 0) AS revenue FROM wards LEFT JOIN (
                    SELECT orders.ward_id, SUM(order_total_price) AS revenue FROM orders
                        LEFT JOIN order_detail od ON orders.order_id = od.order_id
                    WHERE order_status = 4
                    GROUP BY orders.ward_id
                ) AS o ON o.ward_id = wards.ward_code WHERE district_id = ?
                ORDER BY revenue DESC, ward_code;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> StatisticalByWard.builder()
                .id(rs.getInt("ward_code"))
                .name(rs.getString("ward_name"))
                .revenue(rs.getInt("revenue"))
                .build(), districtId);
    }

    public List<StatisticalByCustomerProvince> getChartUserProvince() {
        String sql = """
                SELECT p.province_code, p.province_name, COUNT(c.customer_id) AS count FROM provinces p
                    LEFT JOIN districts d on p.province_code = d.province_id
                    LEFT JOIN wards w on d.district_code = w.district_id
                    LEFT JOIN customers c on w.ward_code = c.ward_id
                GROUP BY p.province_code;
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> StatisticalByCustomerProvince.builder()
                .id(rs.getInt("province_code"))
                .name(rs.getString("province_name"))
                .count(rs.getInt("count"))
                .build());
    }

    public List<StatisticalByCustomerDistrict> getChartUserDistrict(int provinceId) {
        String sql = """
                SELECT d.district_code, d.district_name, COUNT(c.customer_id) AS count FROM districts d
                    LEFT JOIN wards w on d.district_code = w.district_id
                    LEFT JOIN customers c on w.ward_code = c.ward_id
                WHERE d.province_id = ?
                GROUP BY d.district_code;
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> StatisticalByCustomerDistrict.builder()
                .id(rs.getInt("district_code"))
                .name(rs.getString("district_name"))
                .count(rs.getInt("count"))
                .build(), provinceId);
    }

    public List<StatisticalByCustomerWard> getChartUserWard(int districtId) {
        String sql = """
                SELECT w.ward_code, w.ward_name, COUNT(c.customer_id) AS count FROM wards w
                    LEFT JOIN customers c on w.ward_code = c.ward_id
                WHERE w.district_id = ?
                GROUP BY w.ward_code;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> StatisticalByCustomerWard.builder()
                .id(rs.getInt("ward_code"))
                .name(rs.getString("ward_name"))
                .count(rs.getInt("count"))
                .build(), districtId);
    }

    public List<ChartByCategory> getChartRevenueByCategory() {
        String sql = """
                SELECT c.category_name, COALESCE(s.totalPrice, 0) as revenue FROM categories c LEFT JOIN (
                    SELECT c1.category_id, SUM(od.order_total_price) AS totalPrice FROM categories c1
                    LEFT JOIN books_categories bc on c1.category_id = bc.category_id
                    LEFT JOIN books b on b.book_id = bc.book_id
                    LEFT JOIN options o on b.book_id = o.book_id
                    LEFT JOIN order_detail od on o.option_id = od.option_id
                    LEFT JOIN orders o2 on o2.order_id = od.order_id
                    WHERE o2.order_status = 4
                    GROUP BY c1.category_id
                ) AS s ON s.category_id = c.category_id;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> ChartByCategory.builder()
                .name(rs.getString("category_name"))
                .revenue(rs.getInt("revenue"))
                .build());
    }
}
