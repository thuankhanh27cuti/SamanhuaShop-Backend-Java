package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> findById(int id) {
        return ResponseEntity.of(customerRepository.findEagerById(id)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class)));
    }

    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(customerRepository.findAllLazy(pageable));
    }

    public ResponseEntity<?> count() {
        return ResponseEntity.ok(customerRepository.count());
    }

    public ResponseEntity<?> findByRoles(Role role, Pageable pageable) {
        return ResponseEntity.ok(customerRepository.findByRoles(role, pageable));
    }

    public ResponseEntity<?> countByRole(Role role) {
        return ResponseEntity.ok(customerRepository.countByRole(role));
    }
}
