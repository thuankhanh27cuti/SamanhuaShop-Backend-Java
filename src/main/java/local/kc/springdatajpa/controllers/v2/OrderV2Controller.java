package local.kc.springdatajpa.controllers.v2;

import local.kc.springdatajpa.services.v2.OrderV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/orders")
public class OrderV2Controller {
    private final OrderV2Service orderService;

    @Autowired
    public OrderV2Controller(OrderV2Service orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id, @RequestHeader(name = "Authorization") String authorization) {
        return orderService.findById(id, authorization);
    }

    @GetMapping("/by-customer/{id}")
    public ResponseEntity<?> findByCustomerId(@PathVariable(name = "id") int id, Pageable pageable) {
        return orderService.findByCustomerId(id, pageable);
    }

    @GetMapping("/by-customer/{id}/by-status/{status}")
    public ResponseEntity<?> findByCustomerIdAndStatus(@PathVariable(name = "id") int id, @PathVariable(name = "status") int status, Pageable pageable) {
        return orderService.findByCustomerIdAndStatus(id, status, pageable);
    }

    @GetMapping("/count/by-customer/{id}")
    public ResponseEntity<?> countByCustomerId(@PathVariable(name = "id") int id) {
        return orderService.countByCustomerId(id);
    }

    @GetMapping("/count/by-customer/{id}/by-status/{status}")
    public ResponseEntity<?> countByCustomerIdAndStatusStatus(@PathVariable(name = "id") int id, @PathVariable(name = "status") int status) {
        return orderService.countByCustomerIdAndStatusStatus(id, status);
    }

    @PutMapping("/by-status/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable(name = "id") int id, @RequestParam(name = "status") int status) {
        return orderService.updateOrderStatus(id, status);
    }
}
