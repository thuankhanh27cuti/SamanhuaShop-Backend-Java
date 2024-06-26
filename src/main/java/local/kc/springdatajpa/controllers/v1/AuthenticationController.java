package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.services.v1.AuthenticationService;
import local.kc.springdatajpa.utils.ChangePasswordRequest;
import local.kc.springdatajpa.utils.authentication.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader(value = "Authorization") String authorization, @RequestBody ChangePasswordRequest request) {
        return authenticationService.changePassword(authorization, request);
    }

    @PostMapping("/refresh-token")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<?> refreshToken(@RequestHeader(value = "Authorization") String authorization) {
        return authenticationService.refreshToken(authorization);
    }

    @PostMapping("/is-token-valid")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<?> isTokenValid(@RequestHeader(value = "Authorization") String authorization) {
        return authenticationService.isTokenValid(authorization);
    }

    @GetMapping("/get-order")
    public ResponseEntity<?> getOrderByUser(@RequestHeader(value = "Authorization") String authorization, Pageable pageable) {
        return authenticationService.getOrderByUser(authorization, pageable);
    }

    @GetMapping("/get-user")
    public ResponseEntity<?> getUser(@RequestHeader(value = "Authorization") String authorization) {
        return authenticationService.getUser(authorization);
    }
}
