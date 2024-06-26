package local.kc.springdatajpa.services.v2;

import local.kc.springdatajpa.daos.OrderJDBC;
import local.kc.springdatajpa.dtos.OrderDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderV2Service {
    private final ModelMapper modelMapper;
    private final OrderJDBC orderJDBC;

    public OrderV2Service(ModelMapper modelMapper, OrderJDBC orderJDBC) {
        this.modelMapper = modelMapper;
        this.orderJDBC = orderJDBC;
    }

    public ResponseEntity<?> findByCustomerId(int id, Pageable pageable) {
        return ResponseEntity.ok(orderJDBC.findByCustomerId(id, pageable).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList());
    }

    public ResponseEntity<?> findById(int id) {
        return ResponseEntity.of(orderJDBC.findById(id)
                .map(order -> modelMapper.map(order, OrderDTO.class)));
    }
}
