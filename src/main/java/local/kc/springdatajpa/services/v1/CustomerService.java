package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.converters.RoleConverter;
import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.v1.CustomerRepository;
import local.kc.springdatajpa.repositories.v1.DistrictRepository;
import local.kc.springdatajpa.repositories.v1.ProvinceRepository;
import local.kc.springdatajpa.repositories.v1.WardRepository;
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
    private final RoleConverter roleConverter;
    private final WardRepository wardRepository;
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           ModelMapper modelMapper, PasswordEncoder passwordEncoder, WardRepository wardRepository, DistrictRepository districtRepository, ProvinceRepository provinceRepository) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.wardRepository = wardRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.roleConverter = new RoleConverter();
    }

    public ResponseEntity<?> findById(int id) {
        Customer customer = customerRepository.findByIdLazy(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        Ward ward = wardRepository.findByCustomerId(id).map(w -> Ward.builder()
                .code(w.getCode())
                .name(w.getName())
                .fullName(w.getFullName())
                .district(districtRepository.findByWardId(w.getCode()).map(d -> District.builder()
                        .code(d.getCode())
                        .name(d.getName())
                        .fullName(d.getFullName())
                        .province(provinceRepository.findByDistrictId(d.getCode()).map(p -> Province.builder()
                                .code(p.getCode())
                                .name(p.getName())
                                .fullName(p.getFullName())
                                .build()).orElse(null))
                        .build()).orElse(null))
                .build()).orElse(null);

        customer.setWard(ward);
        return ResponseEntity.ok(modelMapper.map(customer, CustomerDTO.class));
    }

    public ResponseEntity<?> findByIdLazy(int id) {
        return ResponseEntity.of(customerRepository.findByIdLazy(id)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class)));
    }

    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(customerRepository.findAllLazy(pageable)
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .toList());
    }

    public ResponseEntity<?> findByRoles(int value, Pageable pageable) {
        Role role = roleConverter.convertToEntityAttribute(value);
        return ResponseEntity.ok(customerRepository.findByRoles(role, pageable)
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .toList());
    }

    public ResponseEntity<?> count() {
        return ResponseEntity.ok(customerRepository.count());
    }

    public ResponseEntity<?> countByRole(int value) {
        Role role = roleConverter.convertToEntityAttribute(value);
        return ResponseEntity.ok(customerRepository.countByRole(role));
    }

    public ResponseEntity<?> saveCustomer(CustomerDTO customerDTO) {
        Ward ward = customerDTO.getWard() != null ? new Ward(customerDTO.getWard().getCode()) : null;
        Customer customer = Customer.builder()
                .name(customerDTO.getName())
                .image(customerDTO.getImage())
                .gender(customerDTO.getGender())
                .phone(customerDTO.getPhone())
                .birthday(customerDTO.getBirthday())
                .username(customerDTO.getUsername())
                .password(passwordEncoder.encode(customerDTO.getPassword()))
                .role(customerDTO.getRole())
                .isDeleted(false)
                .ward(ward)
                .build();
        customerRepository.save(customer);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> updateCustomer(int id, CustomerDTO customerDTO) {
        Ward ward = customerDTO.getWard() != null ? new Ward(customerDTO.getWard().getCode()) : null;
        System.out.println(ward);

        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        customer.setName(customerDTO.getName());
        customer.setImage(customerDTO.getImage());
        customer.setBirthday(customerDTO.getBirthday());
        customer.setGender(customerDTO.getGender());
        customer.setPhone(customerDTO.getPhone());
        customer.setRole(customerDTO.getRole());
        customer.setWard(ward);
        customerRepository.save(customer);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteCustomer(int id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        customer.setDeleted(!customer.isDeleted());
        customerRepository.save(customer);
        return ResponseEntity.ok().build();
    }
}
