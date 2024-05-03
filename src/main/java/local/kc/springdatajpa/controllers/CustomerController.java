package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(Pageable pageable) {
        return customerService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id) {
        return customerService.findById(id);
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        return customerService.count();
    }

    @GetMapping("/count/by-role/{role}")
    public ResponseEntity<?> countByRole(@PathVariable(name = "role") Role role) {
        return customerService.countByRole(role);
    }

    @GetMapping("/by-role/{role}")
    public ResponseEntity<?> findByRoles(@PathVariable(name = "role") Role role, Pageable pageable) {
        return customerService.findByRoles(role, pageable);
    }

}
