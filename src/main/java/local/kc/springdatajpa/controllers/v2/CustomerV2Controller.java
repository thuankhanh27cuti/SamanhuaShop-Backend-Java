package local.kc.springdatajpa.controllers.v2;

import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.services.v2.CustomerV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable(name = "id") int id, @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(id, customerDTO);
    }
}
