package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.OrderDetailRepository;
import local.kc.springdatajpa.repositories.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ModelMapper modelMapper,
                        OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.orderDetailRepository = orderDetailRepository;
    }

    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(orderRepository.findAllLazy(pageable)
                .stream()
                .peek(order -> {
                    order.setOrderDetails(null);
                    Customer customer = order.getCustomer();
                    customer.setOrders(new HashSet<>(Set.of(order)));
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
                    customer.setOrders(new HashSet<>(Set.of(order)));
                })
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList());
    }

    public ResponseEntity<?> findByCustomerId(int id, Pageable pageable) {
        return ResponseEntity
                .ok(orderRepository.findByCustomerIdLazy(id, pageable).stream()
                        .peek(order -> {
                            Set<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
                            Set<OrderDetail> set = orderDetails.stream()
                                    .peek(orderDetailConsumer)
                                    .collect(Collectors.toSet());
                            order.setOrderDetails(set);
                        })
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
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> findById(int id) {
        return orderRepository.findByIdLazy(id)
                .map(order -> {
                    Customer customer = order.getCustomer();
                    customer.setOrders(new HashSet<>());
                    Set<OrderDetail> orderDetails = order.getOrderDetails();
                    orderDetails.forEach(orderDetailConsumer);
                    return order;
                })
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public static final Consumer<OrderDetail> orderDetailConsumer = orderDetail -> {
        Option option = orderDetail.getOption();
        Book book = option.getBook();
        book.setOptions(null);
        book.setCategories(null);
        book.setImages(null);
        option.setBook(book);
        option.setOrdersDetails(null);
        orderDetail.setOption(option);
        orderDetail.setOrder(null);
    };

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
}