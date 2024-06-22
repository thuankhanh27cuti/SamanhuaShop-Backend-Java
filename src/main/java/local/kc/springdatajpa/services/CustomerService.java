package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.models.Customer;
import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> findById(int id) {
        return ResponseEntity.of(customerRepository.findLazyById(id)
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

    public ResponseEntity<?> saveCustomer(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .name(customerDTO.getName())
                .image(customerDTO.getImage())
                .gender(customerDTO.getGender())
                .phone(customerDTO.getPhone())
                .username(customerDTO.getUsername())
                .password(passwordEncoder.encode(customerDTO.getPassword()))
                .role(customerDTO.getRole())
                .build();
        customerRepository.save(customer);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> updateCustomer(int id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        customer.setName(customerDTO.getName());
        customer.setImage(customerDTO.getImage());
        customer.setGender(customerDTO.getGender());
        customer.setPhone(customerDTO.getPhone());
        customer.setRole(customerDTO.getRole());
        customerRepository.save(customer);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteCustomer(int id) {
        customerRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
