package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.services.v1.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable) {
        return orderService.findAll(pageable);
    }

    @GetMapping("/by-customer/{id}")
    public ResponseEntity<?> findByCustomerId(@PathVariable(name = "id") int id, Pageable pageable) {
        return orderService.findByCustomerId(id, pageable);
    }

    @GetMapping("/by-customer-lazy/{id}")
    public ResponseEntity<?> findByCustomerIdLazy(@PathVariable(name = "id") int id, Pageable pageable) {
        return orderService.findByCustomerIdLazy(id, pageable);
    }

    @GetMapping("/by-customer-lazy/{id}/by-status/{status}")
    public ResponseEntity<?> findByCustomerIdLazyAndStatus(@PathVariable(name = "id") int id, @PathVariable(name = "status") int status, Pageable pageable) {
        return orderService.findByCustomerIdLazyAndStatus(id, status, pageable);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<?> findByOrderStatus(@PathVariable(name = "status") int status, Pageable pageable) {
        return orderService.findByOrderStatus(status, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id) {
        return orderService.findById(id);
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        return orderService.count();
    }

    @GetMapping("/count/by-status/{status}")
    public ResponseEntity<?> countByStatus(@PathVariable(name = "status") int status) {
        return orderService.countByStatus(status);
    }

    @GetMapping("/count/by-customer/{id}")
    public ResponseEntity<?> countByCustomerId(@PathVariable(name = "id") int id) {
        return orderService.countByCustomerId(id);
    }

    @GetMapping("/count/by-customer/{id}/by-status/{status}")
    public ResponseEntity<?> countByCustomerIdAndStatusStatus(@PathVariable(name = "id") int id, @PathVariable(name = "status") int status) {
        return orderService.countByCustomerIdAndStatusStatus(id, status);
    }

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.saveOrder(orderDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable(name = "id") int id, @RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(id, orderDTO);
    }

    @PutMapping("/by-status/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable(name = "id") int id, @RequestParam(name = "status") int status) {
        return orderService.updateOrderStatus(id, status);
    }
}
