package local.kc.springdatajpa.controllers.v2;

import local.kc.springdatajpa.services.v2.OrderV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/orders")
public class OrderV2Controller {
    private final OrderV2Service orderService;

    @Autowired
    public OrderV2Controller(OrderV2Service orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id) {
        return orderService.findById(id);
    }

    @GetMapping("/by-customer/{id}")
    public ResponseEntity<?> findByCustomerId(@PathVariable(name = "id") int id, Pageable pageable) {
        return orderService.findByCustomerId(id, pageable);
    }
}
