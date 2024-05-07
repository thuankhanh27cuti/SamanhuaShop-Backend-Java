package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ModelMapper modelMapper,
                        CustomerRepository customerRepository,
                        OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.customerRepository = customerRepository;
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
        Integer customerId = orderDTO.getCustomer().getId();
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return ResponseEntity.noContent().build();
        }

        Order order = orderRepository.save(Order.builder()
                .customer(customer)
                .consigneeName(orderDTO.getConsigneeName())
                .address(orderDTO.getAddress())
                .phone(orderDTO.getPhone())
                .createAt(new Date())
                .orderStatus(OrderStatus.PENDING)
                .orderDetails(new HashSet<>())
                .build());
        return ResponseEntity.ok(order.getId());
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