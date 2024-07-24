package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.converters.OrderStatusConverter;
import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.v1.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderLogRepository orderLogRepository;
    private final CustomerRepository customerRepository;
    private final OrderStatusConverter orderStatusConverter;
    private final OptionRepository optionRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper, OrderDetailRepository orderDetailRepository, OrderLogRepository orderLogRepository, CustomerRepository customerRepository, OptionRepository optionRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.orderDetailRepository = orderDetailRepository;
        this.orderLogRepository = orderLogRepository;
        this.customerRepository = customerRepository;
        this.orderStatusConverter = new OrderStatusConverter();
        this.optionRepository = optionRepository;
    }

    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(orderRepository.findAllLazy(pageable)
                .stream()
                .peek(order -> customerRepository.findByOrderId(order.getId())
                        .ifPresent(customer -> order.setCustomer(Customer.builder()
                        .id(customer.getId())
                        .username(customer.getUsername())
                        .isDeleted(customer.isDeleted())
                        .build()
                )))
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList());
    }

    public ResponseEntity<?> findByOrderStatus(int status, Pageable pageable) {
        OrderStatus orderStatus = orderStatusConverter.convertToEntityAttribute(status);
        return ResponseEntity.ok(orderRepository.findByOrderStatusLazy(orderStatus, pageable)
                .stream()
                .peek(order -> customerRepository.findByOrderId(order.getId())
                        .ifPresent(customer -> order.setCustomer(Customer.builder()
                        .id(customer.getId())
                        .username(customer.getUsername())
                        .isDeleted(customer.isDeleted())
                        .build()
                )))
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

    public ResponseEntity<?> findByCustomerIdLazyAndStatus(int id, int status, Pageable pageable) {
        OrderStatus orderStatus = orderStatusConverter.convertToEntityAttribute(status);
        return ResponseEntity
                .ok(orderRepository.findByCustomerIdLazyAndStatus(id, orderStatus, pageable).stream()
                        .map(order -> modelMapper.map(order, OrderDTO.class))
                        .toList());
    }

    public ResponseEntity<?> saveOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setCreateAt(new Date());
        List<Option> options = new ArrayList<>();

        boolean allMatch = order.getOrderDetails().stream().allMatch(orderDetail -> {
            Integer optionId = orderDetail.getOrderDetailId().getOptionId();
            Option option = optionRepository.findById(optionId).orElse(null);
            options.add(option);
            return option != null && option.getQuantity() >= orderDetail.getQuantity();
        });

        if (!allMatch) {
            return ResponseEntity.badRequest().build();
        }

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

            options.stream()
                    .filter(option -> option.getId().equals(optionId))
                    .findFirst()
                    .ifPresent(option -> {
                        int quantity = option.getQuantity();
                        option.setQuantity(quantity - orderDetail.getQuantity());
                        optionRepository.save(option);
                    });
            orderDetailRepository.save(orderDetail1);
        });

        OrderStatus orderStatus = orderDTO.getOrderStatus();

        saveOrderLog(orderId, orderStatus);

        return ResponseEntity.ok(orderId);
    }

    public ResponseEntity<?> findById(int id) {
        return ResponseEntity.of(orderRepository.findById(id).map(order -> {
            Customer customer = order.getCustomer();
            Ward ward = order.getWard();
            District district = order.getDistrict();
            Province province = order.getProvince();

            order.setOrderDetails(new HashSet<>());

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

    public ResponseEntity<?> countByStatus(int status) {
        OrderStatus orderStatus = orderStatusConverter.convertToEntityAttribute(status);
        return ResponseEntity.ok(orderRepository.countByStatus(orderStatus));
    }

    public ResponseEntity<?> countByCustomerId(int id) {
        return ResponseEntity.ok(orderRepository.countByCustomerId(id));
    }

    public ResponseEntity<?> countByCustomerIdAndStatusStatus(int id, int status) {
        OrderStatus orderStatus = orderStatusConverter.convertToEntityAttribute(status);
        return ResponseEntity.ok(orderRepository.countByCustomerIdAndStatus(id, orderStatus));
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

        saveOrderLog(id, orderStatus);

        orderRepository.save(order);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> updateOrderStatus(int id, int status) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        OrderStatus orderStatus = orderStatusConverter.convertToEntityAttribute(status);
        System.out.println(orderStatus);

        boolean isUpdated = switch (order.getOrderStatus()) {
            case WAIT_FOR_PAY -> orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.DECLINED;
            case PENDING -> orderStatus == OrderStatus.PREPARING;
            case PREPARING -> orderStatus == OrderStatus.SHIPPING;
            case SHIPPING -> orderStatus == OrderStatus.SUCCESS || orderStatus == OrderStatus.DECLINED;
            default -> false;
        };

        if (isUpdated) {
            order.setOrderStatus(orderStatus);
            saveOrderLog(id, orderStatus);

            if (orderStatus == OrderStatus.SUCCESS || orderStatus == OrderStatus.DECLINED) {
                order.setFinishedAt(new Date());
            }

            if (orderStatus == OrderStatus.DECLINED) {
                Set<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(id);
                orderDetails.forEach(orderDetail -> {
                    Option option = orderDetail.getOption();
                    option.setQuantity(option.getQuantity() + orderDetail.getQuantity());
                    optionRepository.save(option);
                });
            }

            orderRepository.save(order);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private void saveOrderLog(Integer orderId, OrderStatus orderStatus) {
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
    }
}