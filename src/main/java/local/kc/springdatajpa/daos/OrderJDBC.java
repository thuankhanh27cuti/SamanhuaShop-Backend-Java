package local.kc.springdatajpa.daos;

import local.kc.springdatajpa.converters.OrderStatusConverter;
import local.kc.springdatajpa.converters.PaymentMethodConverter;
import local.kc.springdatajpa.models.Order;
import local.kc.springdatajpa.repositories.v1.DistrictRepository;
import local.kc.springdatajpa.repositories.v1.OrderLogRepository;
import local.kc.springdatajpa.repositories.v1.ProvinceRepository;
import local.kc.springdatajpa.repositories.v1.WardRepository;
import local.kc.springdatajpa.utils.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderJDBC {
    private final JdbcTemplate jdbcTemplate;
    private final OrderStatusConverter orderStatusConverter;
    private final PaymentMethodConverter paymentMethodConverter;
    private final OrderDetailJDBC orderDetailJDBC;
    private final CustomerJDBC customerJDBC;
    private final WardRepository wardRepository;
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;
    private final OrderLogRepository orderLogRepository;

    @Autowired
    public OrderJDBC(JdbcTemplate jdbcTemplate, OrderDetailJDBC orderDetailJDBC, CustomerJDBC customerJDBC, WardRepository wardRepository, DistrictRepository districtRepository, ProvinceRepository provinceRepository, OrderLogRepository orderLogRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderDetailJDBC = orderDetailJDBC;
        this.customerJDBC = customerJDBC;
        this.orderStatusConverter = new OrderStatusConverter();
        this.paymentMethodConverter = new PaymentMethodConverter();
        this.wardRepository = wardRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.orderLogRepository = orderLogRepository;
    }

    public List<Order> findByCustomerId(int id, Pageable pageable) {
        String sql = """
                SELECT orders.order_id AS id, orders.order_created_at, orders.order_finished_at, orders.order_status FROM orders
                    LEFT JOIN customers c on c.customer_id = orders.customer_id
                WHERE c.customer_id = ?
                """;

        if (pageable != null) {
            int pageNumber = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();
            int start = pageNumber * pageSize;

            sql = new QueryBuilder.Builder()
                    .select(sql)
                    .sorted(pageable.getSort())
                    .limit(start, pageSize)
                    .build();
        }

        return jdbcTemplate.query(sql, (rs, rowNum) -> Order.builder()
                .id(rs.getInt("id"))
                .createAt(rs.getTimestamp("order_created_at"))
                .finishedAt(rs.getTimestamp("order_finished_at"))
                .orderStatus(orderStatusConverter.convertToEntityAttribute(rs.getInt("order_status")))
                .orderDetails(new HashSet<>(orderDetailJDBC.findByOrderId(rs.getInt("id"))))
                .build(), id);
    }

    public List<Order> findByCustomerIdAndStatus(int id, int status, Pageable pageable) {
        String sql = """
                SELECT orders.order_id AS id, orders.order_created_at, orders.order_finished_at, orders.order_status FROM orders
                    LEFT JOIN customers c on c.customer_id = orders.customer_id
                WHERE c.customer_id = ? AND orders.order_status = ?
                """;

        if (pageable != null) {
            int pageNumber = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();
            int start = pageNumber * pageSize;

            sql = new QueryBuilder.Builder()
                    .select(sql)
                    .sorted(pageable.getSort())
                    .limit(start, pageSize)
                    .build();
        }

        return jdbcTemplate.query(sql, (rs, rowNum) -> Order.builder()
                .id(rs.getInt("id"))
                .createAt(rs.getTimestamp("order_created_at"))
                .finishedAt(rs.getTimestamp("order_finished_at"))
                .orderStatus(orderStatusConverter.convertToEntityAttribute(rs.getInt("order_status")))
                .orderDetails(new HashSet<>(orderDetailJDBC.findByOrderId(rs.getInt("id")))).build(), id, status);
    }

    public Optional<Order> findById(int id, Integer customerId) {
        String sql = """
                SELECT order_id as id, order_address_detail, order_consignee_name, order_created_at, order_finished_at, order_status, order_payment_method, order_phone, customer_id, district_id, province_id, ward_id FROM orders WHERE order_id = ? AND customer_id = ?
                """;
        List<Order> orders = jdbcTemplate.query(sql, (rs, rowNum) -> Order.builder()
                .id(rs.getInt("id"))
                .customer(customerJDBC.findById(rs.getInt("customer_id")).orElse(null))
                .address(rs.getString("order_address_detail"))
                .consigneeName(rs.getString("order_consignee_name"))
                .createAt(rs.getTimestamp("order_created_at"))
                .finishedAt(rs.getTimestamp("order_finished_at"))
                .orderStatus(orderStatusConverter.convertToEntityAttribute(rs.getInt("order_status")))
                .paymentMethod(paymentMethodConverter.convertToEntityAttribute(rs.getInt("order_payment_method")))
                .phone(rs.getString("order_phone"))
                .orderDetails(new HashSet<>(orderDetailJDBC.findByOrderId(rs.getInt("id"))))
                .ward(wardRepository.findByIdLazy(rs.getInt("ward_id")).orElse(null))
                .district(districtRepository.findByIdLazy(rs.getInt("district_id")).orElse(null))
                .province(provinceRepository.findByIdLazy(rs.getInt("province_id")).orElse(null))
                .orderLogs(new HashSet<>(orderLogRepository.findByOrderId(rs.getInt("id"))))
                .build(), id, customerId);
        return orders.isEmpty() ? Optional.empty() : Optional.of(orders.get(0));
    }
}
