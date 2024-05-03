package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("permitAll()")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/all-roles")
    public ResponseEntity<?> getAllRoles() {
        return adminService.getAllRoles();
    }

    @GetMapping("/top-seller")
    public ResponseEntity<?> getTopSeller(Pageable pageable) {
        Pageable pageRequest = PageRequest.of(0, 5, pageable.getSort());
        return adminService.getTopSeller(pageRequest);
    }

    @GetMapping("/book-status")
    public ResponseEntity<?> getBookStatus() {
        return adminService.getBookStatus();
    }

    @GetMapping("/today-featured")
    public ResponseEntity<?> getTodayFeatured() {
        return adminService.getTodayFeatured();
    }

    @GetMapping("/revenue-by-week")
    public ResponseEntity<?> getRevenueByWeek() {
        return adminService.getRevenueByWeek();
    }
}
