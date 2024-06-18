package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.repositories.OrderDetailRepository;
import local.kc.springdatajpa.repositories.OrderRepository;
import local.kc.springdatajpa.utils.ChangePasswordRequest;
import local.kc.springdatajpa.utils.ErrorRes;
import local.kc.springdatajpa.utils.authentication.AuthenticationRequest;
import local.kc.springdatajpa.utils.authentication.AuthenticationResponse;
import local.kc.springdatajpa.config.security.JwtService;
import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static local.kc.springdatajpa.services.OrderService.orderDetailConsumer;

@Service
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public AuthenticationService(CustomerRepository customerRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder,
                                 ModelMapper modelMapper, JwtService jwtService, AuthenticationManager authenticationManager, OrderDetailRepository orderDetailRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.orderDetailRepository = orderDetailRepository;
    }

    public ResponseEntity<?> register(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setRole(Role.USER);
        customerRepository.save(customer);
        String jwtToken = jwtService.generateToken(customer);
        return ResponseEntity.ok(this.generateAuthenticationResponse(jwtToken, customer));
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Customer customer = customerRepository.findCustomerByUsername(request.getUsername()).orElse(null);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String jwtToken = jwtService.generateToken(customer);
        return ResponseEntity.ok(this.generateAuthenticationResponse(jwtToken, customer));
    }

    public ResponseEntity<?> changePassword(String authorization, ChangePasswordRequest request) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = authorization.substring(7);
        String username = jwtService.extractUsername(jwt);
        Customer customer = customerRepository.findCustomerByUsername(username).orElse(null);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorRes.builder().msg("Can't find customer with username " + username).build());
        }

        if (passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
            customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
            customerRepository.save(customer);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorRes.builder().msg("Wrong password").build());
        }
    }

    public ResponseEntity<?> getUser(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = authorization.substring(7);
        String username = jwtService.extractUsername(jwt);
        return ResponseEntity.of(customerRepository.findCustomerByUsernameLazy(username)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class)));
    }

    public ResponseEntity<?> getOrderByUser(String authorization, Pageable pageable) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = authorization.substring(7);
        String username = jwtService.extractUsername(jwt);
        return ResponseEntity
                .ok(orderRepository.findByUsernameLazy(username, pageable).stream()
                        .peek(order -> {
                            Set<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
                            Set<OrderDetail> set = orderDetails.stream()
                                    .peek(orderDetailConsumer)
                                    .collect(Collectors.toSet());
                            order.setOrderDetails(set);
                        })
                        .map(order -> modelMapper.map(order, OrderDTO.class))
                        .toList());
    }

    public ResponseEntity<?> refreshToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = authorization.substring(7);
        String username = jwtService.extractUsername(jwt);
        return customerRepository.findCustomerByUsername(username)
                .filter(customer -> !jwtService.isTokenExpired(jwt))
                .map(customer -> jwtService.isTokenValid(jwt, customer) ?
                    ResponseEntity.ok(this.generateAuthenticationResponse(jwtService.generateToken(customer), customer)) :
                    ResponseEntity.status(HttpStatus.FORBIDDEN).build())
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    public ResponseEntity<?> isTokenValid(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = authorization.substring(7);
        String username = jwtService.extractUsername(jwt);
        return customerRepository.findCustomerByUsername(username)
                .filter(customer -> !jwtService.isTokenExpired(jwt))
                .map(customer -> jwtService.isTokenValid(jwt, customer) ?
                        ResponseEntity.ok().build() :
                        ResponseEntity.status(HttpStatus.FORBIDDEN).build())
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private AuthenticationResponse generateAuthenticationResponse(String jwtToken, Customer customer) {
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .subject(jwtService.extractUsername(jwtToken))
                .exp(jwtService.extractExpiration(jwtToken))
                .avatar(customer.getImage())
                .role(customer.getRole().toString())
                .build();
    }
}
