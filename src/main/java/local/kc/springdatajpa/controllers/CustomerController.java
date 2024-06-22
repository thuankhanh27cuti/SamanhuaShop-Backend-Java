package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.models.Customer;
import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable(name = "id") int id, @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(id, customerDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable(name = "id") int id) {
        return customerService.deleteCustomer(id);
    }
}
