package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.services.v1.AdminService;
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

    @GetMapping("/all-order-status")
    public ResponseEntity<?> getAllOrderStates() {
        return adminService.getAllOrderStates();
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

    @GetMapping("/out-of-stock")
    public ResponseEntity<?> getOptionOutOfStock(Pageable pageable) {
        return adminService.getOptionOutOfStock(pageable);
    }

    @GetMapping("/out-of-stock/count")
    public ResponseEntity<?> getCountOptionOutOfStock() {
        return adminService.getCountOptionOutOfStock();
    }

    @GetMapping("/valuable-customer/count")
    public ResponseEntity<?> getCountValuableCustomer() {
        return adminService.getCountValuableCustomer();
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

    @GetMapping("/chart/user-province")
    public ResponseEntity<?> getChartUserProvince() {
        return adminService.getChartUserProvince();
    }

    @GetMapping("/chart/user-district/{provinceId}")
    public ResponseEntity<?> getChartUserDistrict(@PathVariable(name = "provinceId") int provinceId) {
        return adminService.getChartUserDistrict(provinceId);
    }

    @GetMapping("/chart/user-ward/{districtId}")
    public ResponseEntity<?> getChartUserWard(@PathVariable(name = "districtId") int districtId) {
        return adminService.getChartUserWard(districtId);
    }

    @GetMapping("/chart/revenue-by-category")
    public ResponseEntity<?> getChartRevenueByCategory() {
        return adminService.getChartRevenueByCategory();
    }

    @GetMapping("/statistical/revenue-by-date")
    public ResponseEntity<?> getStatisticalRevenueByDate(@RequestParam(name = "date") LocalDate date) {
        return adminService.getStatisticalRevenueByDate(date);
    }

    @GetMapping("/statistical/revenue-by-month")
    public ResponseEntity<?> getStatisticalRevenueByMonth(@RequestParam(name = "month") int month, @RequestParam(name = "year") int year) {
        return adminService.getStatisticalRevenueByMonth(month, year);
    }

    @GetMapping("/statistical/revenue-by-year")
    public ResponseEntity<?> getStatisticalRevenueByYear(@RequestParam(name = "year") int year) {
        return adminService.getStatisticalRevenueByYear(year);
    }

    @GetMapping("/statistical/revenue-all-time")
    public ResponseEntity<?> getStatisticalRevenueAllTime() {
        return adminService.getStatisticalRevenueAllTime();
    }

    @GetMapping("/statistical/revenue-by-province")
    public ResponseEntity<?> getStatisticalRevenueByProvince() {
        return adminService.getStatisticalRevenueByProvince();
    }

    @GetMapping("/statistical/revenue-by-district/{provinceId}")
    public ResponseEntity<?> getStatisticalRevenueByDistrict(@PathVariable(name = "provinceId") int provinceId) {
        return adminService.getStatisticalRevenueByDistrict(provinceId);
    }

    @GetMapping("/statistical/revenue-by-ward/{districtId}")
    public ResponseEntity<?> getStatisticalRevenueByWard(@PathVariable(name = "districtId") int districtId) {
        return adminService.getStatisticalRevenueByWard(districtId);
    }
}
