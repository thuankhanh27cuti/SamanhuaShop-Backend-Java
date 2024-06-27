package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.v1.OrderDetailRepository;
import local.kc.springdatajpa.repositories.v1.OrderLogRepository;
import local.kc.springdatajpa.repositories.v1.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderLogRepository orderLogRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper, OrderDetailRepository orderDetailRepository, OrderLogRepository orderLogRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.orderDetailRepository = orderDetailRepository;
        this.orderLogRepository = orderLogRepository;
    }

    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(orderRepository.findAllLazy(pageable)
                .stream()
                .peek(order -> {
                    order.setOrderDetails(null);

                    Customer customer = order.getCustomer();
                    order.setCustomer(Customer.builder()
                            .id(customer.getId())
                            .username(customer.getUsername())
                            .isDeleted(customer.isDeleted())
                            .build()
                    );
                })
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList());
    }

    public ResponseEntity<?> findByOrderStatus(OrderStatus status, Pageable pageable) {
        return ResponseEntity.ok(orderRepository.findByOrderStatusLazy(status, pageable)
                .stream()
                .peek(order -> {
                    order.setOrderDetails(null);

                    Customer customer = order.getCustomer();
                    order.setCustomer(Customer.builder()
                            .id(customer.getId())
                            .username(customer.getUsername())
                            .isDeleted(customer.isDeleted())
                            .build()
                    );
                })
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList());
    }

    public ResponseEntity<?> findByCustomerId(int id, Pageable pageable) {
        return ResponseEntity
                .ok(orderRepository.findByCustomerIdLazy(id, pageable).stream()
                        .peek(order -> order.setOrderDetails(orderDetailRepository.findByOrderId(order.getId())))
                        .map(order -> modelMapper.map(order, OrderDTO.class))
                        .toList());
    }

    public ResponseEntity<?> findByCustomerIdLazy(int id, Pageable pageable) {
        return ResponseEntity
                .ok(orderRepository.findByCustomerIdLazy(id, pageable).stream()
                        .map(order -> modelMapper.map(order, OrderDTO.class))
                        .toList());
    }

    public ResponseEntity<?> findByCustomerIdLazyAndStatus(int id, OrderStatus status, Pageable pageable) {
        return ResponseEntity
                .ok(orderRepository.findByCustomerIdLazyAndStatus(id, status, pageable).stream()
                        .map(order -> modelMapper.map(order, OrderDTO.class))
                        .toList());
    }

    public ResponseEntity<?> saveOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setCreateAt(new Date());
        Integer orderId = orderRepository.save(order).getId();
        order.getOrderDetails().forEach(orderDetail -> {
            Integer optionId = orderDetail.getOrderDetailId().getOptionId();
            OrderDetail orderDetail1 = OrderDetail.builder()
                    .orderDetailId(new OrderDetailId(orderId, optionId))
                    .order(new Order(orderId))
                    .option(new Option(optionId))
                    .price(orderDetail.getPrice())
                    .quantity(orderDetail.getQuantity())
                    .build();
            orderDetailRepository.save(orderDetail1);
        });

        OrderStatus orderStatus = orderDTO.getOrderStatus();

        OrderLog orderLog = OrderLog.builder()
                .time(new Date())
                .order(new Order(orderId))
                .description(switch (orderStatus) {
                    case WAIT_FOR_PAY -> "Chờ thanh toán";
                    case PENDING -> "Đặt hàng thành công";
                    case PREPARING -> "Đơn hàng đang được chuẩn bị";
                    case SHIPPING -> "Đơn hàng bắt đầu được giao";
                    case SUCCESS -> "Đã nhận được hàng";
                    case DECLINED -> "Đơn hàng đã huỷ";
                })
                .build();

        orderLogRepository.save(orderLog);

        return ResponseEntity.ok(orderId);
    }

    public ResponseEntity<?> findById(int id) {
        return ResponseEntity.of(orderRepository.findById(id).map(order -> {
            Customer customer = order.getCustomer();
            Ward ward = order.getWard();
            District district = order.getDistrict();
            Province province = order.getProvince();

            order.setCustomer(Customer.builder()
                    .id(customer.getId())
                    .username(customer.getUsername())
                    .isDeleted(customer.isDeleted())
                    .build()
            );
            order.setWard(Ward.builder()
                    .code(ward.getCode())
                    .name(ward.getName())
                    .fullName(ward.getFullName())
                    .build());
            order.setDistrict(District.builder()
                    .code(district.getCode())
                    .name(district.getName())
                    .fullName(district.getFullName())
                    .build());
            order.setProvince(Province.builder()
                    .code(province.getCode())
                    .name(province.getName())
                    .fullName(province.getFullName())
                    .build());
            order.setOrderLogs(new HashSet<>());

            return order;
        }).map(order -> modelMapper.map(order, OrderDTO.class)));
    }

    public ResponseEntity<?> count() {
        return ResponseEntity.ok(orderRepository.count());
    }

    public ResponseEntity<?> countByStatus(OrderStatus status) {
        return ResponseEntity.ok(orderRepository.countByStatus(status));
    }

    public ResponseEntity<?> countByCustomerId(int id) {
        return ResponseEntity.ok(orderRepository.countByCustomerId(id));
    }

    public ResponseEntity<?> countByCustomerIdAndStatusStatus(int id, OrderStatus status) {
        return ResponseEntity.ok(orderRepository.countByCustomerIdAndStatus(id, status));
    }

    public ResponseEntity<?> updateOrder(int id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ResponseEntity.ok().build();
        }

        order.setConsigneeName(orderDTO.getConsigneeName());
        order.setPhone(orderDTO.getPhone());
        order.setAddress(orderDTO.getAddress());

        OrderStatus orderStatus = orderDTO.getOrderStatus();

        order.setOrderStatus(orderStatus);

        OrderLog orderLog = OrderLog.builder()
                .time(new Date())
                .order(new Order(id))
                .description(switch (orderStatus) {
                    case WAIT_FOR_PAY -> "Chờ thanh toán";
                    case PENDING -> "Đặt hàng thành công";
                    case PREPARING -> "Đơn hàng đang được chuẩn bị";
                    case SHIPPING -> "Đơn hàng bắt đầu được giao";
                    case SUCCESS -> "Đã nhận được hàng";
                    case DECLINED -> "Đơn hàng đã huỷ";
                })
                .build();

        orderLogRepository.save(orderLog);

        orderRepository.save(order);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> updateOrderStatus(int id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ResponseEntity.ok().build();
        }
        order.setOrderStatus(status);

        OrderLog orderLog = OrderLog.builder()
                .time(new Date())
                .order(new Order(id))
                .description(switch (status) {
                    case WAIT_FOR_PAY -> "Chờ thanh toán";
                    case PENDING -> "Đặt hàng thành công";
                    case PREPARING -> "Đơn hàng đang được chuẩn bị";
                    case SHIPPING -> "Đơn hàng bắt đầu được giao";
                    case SUCCESS -> "Đã nhận được hàng";
                    case DECLINED -> "Đơn hàng đã huỷ";
                })
                .build();

        orderLogRepository.save(orderLog);

        orderRepository.save(order);
        return ResponseEntity.ok().build();
    }
}