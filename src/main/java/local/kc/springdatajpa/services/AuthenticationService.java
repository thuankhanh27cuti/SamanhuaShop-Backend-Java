package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.OrderDTO;
import local.kc.springdatajpa.models.*;
import local.kc.springdatajpa.utils.authentication.AuthenticationRequest;
import local.kc.springdatajpa.utils.authentication.AuthenticationResponse;
import local.kc.springdatajpa.config.security.JwtService;
import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                                 ModelMapper modelMapper, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> register(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setRole(Role.USER);
        customerRepository.save(customer);
        String jwtToken = jwtService.generateToken(customer);
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Customer customer = customerRepository.findCustomerByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(customer);
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }

    public ResponseEntity<?> getUser(String authorization) {
        String jwt = authorization.substring(7);
        String username = jwtService.extractUsername(jwt);
        return customerRepository.findCustomerByUsernameLazy(username)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> getOrderByUser(String authorization) {
        String jwt = authorization.substring(7);
        String username = jwtService.extractUsername(jwt);
        return customerRepository.findEagerByUsername(username)
                .map(customer -> {
                    Set<Order> orders = customer.getOrders();
                    orders.forEach(order -> {
                        System.out.println(order.getId());
                        Set<OrderDetail> orderDetails = order.getOrderDetails();
                        orderDetails.forEach(orderDetail -> {
                            Option option = orderDetail.getOption();
                            option.setOrdersDetails(new HashSet<>(Set.of(orderDetail)));
                            Book book = option.getBook();
                            book.setOptions(new HashSet<>(Set.of(option)));
                            book.setImages(new HashSet<>());
                            book.setCategories(new HashSet<>());
                        });
                    });
                    return orders;
                })
                .map(orders -> orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
                        .collect(Collectors.toSet()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
                    ResponseEntity.ok(jwtService.generateToken(customer)) :
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
}
