package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.services.AuthenticationService;
import local.kc.springdatajpa.utils.authentication.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomerDTO customerDTO) {
        return authenticationService.register(customerDTO);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @GetMapping("/get-order")
    public ResponseEntity<?> getOrderByUser(@RequestHeader(value = "Authorization") String authorization) {
        return authenticationService.getOrderByUser(authorization);
    }

    @GetMapping("/get-user")
    public ResponseEntity<?> getUser(@RequestHeader(value = "Authorization") String authorization) {
        return authenticationService.getUser(authorization);
    }
}
