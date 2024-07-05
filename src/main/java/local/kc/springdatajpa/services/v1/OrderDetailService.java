package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.dtos.OrderDetailDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.v1.OptionRepository;
import local.kc.springdatajpa.repositories.v1.OrderDetailRepository;
import local.kc.springdatajpa.repositories.v1.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderDetailService(OrderDetailRepository orderDetailRepository,
                              OrderRepository orderRepository,
                              OptionRepository optionRepository,
                              ModelMapper modelMapper) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> saveOrderDetail(OrderDetailDTO orderDetailDTO) {
        Option option = optionRepository.findById(orderDetailDTO.getOption().getId()).orElse(null);
        Order order = orderRepository.findById(orderDetailDTO.getOrder().getId()).orElse(null);
        if (option == null || order == null) {
            return ResponseEntity.noContent().build();
        }
        OrderDetail orderDetail = OrderDetail.builder()
            .orderDetailId(new OrderDetailId(order.getId(), option.getId()))
            .order(order)
            .option(option)
            .price(orderDetailDTO.getPrice())
            .quantity(orderDetailDTO.getQuantity())
            .build();
        orderDetailRepository.save(orderDetail);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> findByOrderId(int id) {
        return ResponseEntity.ok(orderDetailRepository.findByOrderId(id).stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailDTO.class))
                .collect(Collectors.toSet()));
    }

    public ResponseEntity<?> countByOrderId(int id) {
        return ResponseEntity.ok(orderDetailRepository.countByOrderId(id));
    }

    public ResponseEntity<?> getRevenuesOfCustomerById(int id) {
        return ResponseEntity.ok(orderDetailRepository.getRevenuesOfCustomerById(id));
    }
}
