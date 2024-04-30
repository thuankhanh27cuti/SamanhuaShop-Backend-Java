package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.dtos.OrderDetailDTO;
import local.kc.springdatajpa.services.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @Autowired
    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @PreAuthorize(value = "isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> saveOrderDetail(@RequestBody OrderDetailDTO orderDetailDTO) {
        return orderDetailService.saveOrderDetail(orderDetailDTO);
    }

    @GetMapping("/by-order/{id}")
    public ResponseEntity<?> findByOrderId(@PathVariable(name = "id") int id) {
        return orderDetailService.findByOrderId(id);
    }
}
