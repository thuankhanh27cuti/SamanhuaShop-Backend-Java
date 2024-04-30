package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.OrderDetailDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.OptionRepository;
import local.kc.springdatajpa.repositories.OrderDetailRepository;
import local.kc.springdatajpa.repositories.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
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
        Set<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(id);
        orderDetails.forEach(OrderService.orderDetailConsumer);
        Set<OrderDetailDTO> orderDetailDTOS = orderDetails.stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailDTO.class))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(orderDetailDTOS);
    }
}
