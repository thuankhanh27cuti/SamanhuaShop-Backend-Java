package local.kc.springdatajpa.services.v2;

import local.kc.springdatajpa.config.security.JwtService;
import local.kc.springdatajpa.converters.OrderStatusConverter;
import local.kc.springdatajpa.daos.OrderJDBC;
import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.v1.CustomerRepository;
import local.kc.springdatajpa.repositories.v1.OptionRepository;
import local.kc.springdatajpa.repositories.v1.OrderDetailRepository;
import local.kc.springdatajpa.repositories.v1.OrderLogRepository;
import local.kc.springdatajpa.repositories.v2.OrderV2Repository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
public class OrderV2Service {
    private final ModelMapper modelMapper;
    private final OrderJDBC orderJDBC;
    private final OrderV2Repository orderRepository;
    private final OrderLogRepository orderLogRepository;
    private final OrderStatusConverter orderStatusConverter;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OptionRepository optionRepository;

    public OrderV2Service(ModelMapper modelMapper, OrderJDBC orderJDBC, OrderV2Repository orderRepository, OrderLogRepository orderLogRepository, JwtService jwtService, CustomerRepository customerRepository, OrderDetailRepository orderDetailRepository, OptionRepository optionRepository) {
        this.modelMapper = modelMapper;
        this.orderJDBC = orderJDBC;
        this.orderRepository = orderRepository;
        this.orderLogRepository = orderLogRepository;
        this.orderStatusConverter = new OrderStatusConverter();
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.optionRepository = optionRepository;
    }

    public ResponseEntity<?> findByCustomerId(int id, Pageable pageable) {
        return ResponseEntity.ok(orderJDBC.findByCustomerId(id, pageable).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList());
    }

    public ResponseEntity<?> findByCustomerIdAndStatus(int id, int status, Pageable pageable) {
        return ResponseEntity.ok(orderJDBC.findByCustomerIdAndStatus(id, status, pageable).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList());
    }

    public ResponseEntity<?> findById(int id, String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = authorization.substring(7);
        String username = jwtService.extractUsername(jwt);
        Customer customer = customerRepository.findCustomerByUsername(username).orElse(null);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer customerId = customer.getId();
        return ResponseEntity.of(orderJDBC.findById(id, customerId)
                .map(order -> modelMapper.map(order, OrderDTO.class)));
    }

    public ResponseEntity<?> countByCustomerId(int id) {
        long countByCustomerId = orderRepository.countByCustomerId(id);
        return ResponseEntity.ok(countByCustomerId);
    }

    public ResponseEntity<?> countByCustomerIdAndStatusStatus(int id, int status) {
        OrderStatus orderStatus = orderStatusConverter.convertToEntityAttribute(status);
        long countByCustomerIdAndOrderStatus = orderRepository.countByCustomerIdAndOrderStatus(id, orderStatus);
        return ResponseEntity.ok(countByCustomerIdAndOrderStatus);
    }

    public ResponseEntity<?> updateOrderStatus(int id, int status) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        OrderStatus orderStatus = orderStatusConverter.convertToEntityAttribute(status);

        if (order.getOrderStatus() == OrderStatus.WAIT_FOR_PAY || order.getOrderStatus() == OrderStatus.PENDING) {
            if (orderStatus == OrderStatus.DECLINED) {
                order.setOrderStatus(orderStatus);
                order.setFinishedAt(new Date());
                OrderLog orderLog = OrderLog.builder()
                        .time(new Date())
                        .order(new Order(id))
                        .description("Đơn hàng đã huỷ")
                        .build();

                orderLogRepository.save(orderLog);

                Set<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(id);
                orderDetails.forEach(orderDetail -> {
                    Option option = orderDetail.getOption();
                    option.setQuantity(option.getQuantity() + orderDetail.getQuantity());
                    optionRepository.save(option);
                });

                orderRepository.save(order);
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
