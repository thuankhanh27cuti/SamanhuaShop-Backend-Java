package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        return customerRepository.findEagerById(id)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
