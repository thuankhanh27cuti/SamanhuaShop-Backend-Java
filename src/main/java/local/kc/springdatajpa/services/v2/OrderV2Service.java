package local.kc.springdatajpa.services.v2;

import local.kc.springdatajpa.converters.OrderStatusConverter;
import local.kc.springdatajpa.daos.OrderJDBC;
import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.models.OrderStatus;
import local.kc.springdatajpa.repositories.v2.OrderV2Repository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderV2Service {
    private final ModelMapper modelMapper;
    private final OrderJDBC orderJDBC;
    private final OrderV2Repository orderRepository;
    private final OrderStatusConverter orderStatusConverter;

    public OrderV2Service(ModelMapper modelMapper, OrderJDBC orderJDBC, OrderV2Repository orderRepository) {
        this.modelMapper = modelMapper;
        this.orderJDBC = orderJDBC;
        this.orderRepository = orderRepository;
        this.orderStatusConverter = new OrderStatusConverter();
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

    public ResponseEntity<?> findById(int id) {
        return ResponseEntity.of(orderJDBC.findById(id)
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
}
