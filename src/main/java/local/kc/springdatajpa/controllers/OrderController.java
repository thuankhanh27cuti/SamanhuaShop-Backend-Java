package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id) {
        return orderService.findById(id);
    }

    @GetMapping("/by-customer/{id}")
    public ResponseEntity<?> findOrderByCustomerId(@PathVariable(name = "id") int id) {
        return orderService.findOrderByCustomerId(id);
    }

    @PreAuthorize(value = "isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.saveOrder(orderDTO);
    }
}
