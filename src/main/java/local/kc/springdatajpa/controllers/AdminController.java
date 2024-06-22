package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin")
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

    @GetMapping("/revenue-by-date")
    public ResponseEntity<?> getRevenueByDate(@RequestParam(name = "date") LocalDate date) {
        return adminService.getRevenueByDate(date);
    }

    @GetMapping("/revenue-by-week")
    public ResponseEntity<?> getRevenueByWeek() {
        return adminService.getRevenueByWeek();
    }

    @GetMapping("/revenue-by-month")
    public ResponseEntity<?> getRevenueByMonth(@RequestParam(name = "month") int month, @RequestParam(name = "year") int year) {
        return adminService.getRevenueByMonth(month, year);
    }

    @GetMapping("/revenue-by-year")
    public ResponseEntity<?> getRevenueByYear(@RequestParam(name = "year") int year) {
        return adminService.getRevenueByYear(year);
    }

    @GetMapping("/revenue-all-time")
    public ResponseEntity<?> getRevenueAllTime() {
        return adminService.getRevenueAllTime();
    }

    @GetMapping("/customer-statistical")
    public ResponseEntity<?> getCustomerStatistical(Pageable pageable) {
        return adminService.getCustomerStatistical(pageable);
    }

    @GetMapping("/customer-statistical/by-role/{role}")
    public ResponseEntity<?> getCustomerStatisticalByRole(@PathVariable(name = "role") Role role, Pageable pageable) {
        return adminService.getCustomerStatisticalByRole(role, pageable);
    }
}
