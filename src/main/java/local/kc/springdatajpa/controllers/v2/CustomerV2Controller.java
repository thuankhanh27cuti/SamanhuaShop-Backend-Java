package local.kc.springdatajpa.controllers.v2;

import local.kc.springdatajpa.services.v2.CustomerV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/customers")
public class CustomerV2Controller {
    private final CustomerV2Service customerService;

    @Autowired
    public CustomerV2Controller(CustomerV2Service customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id) {
        return customerService.findById(id);
    }
}
