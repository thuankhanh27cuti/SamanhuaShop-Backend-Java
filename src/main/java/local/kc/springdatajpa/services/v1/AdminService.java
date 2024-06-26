package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.repositories.v1.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdminService {
    private final GenericRepository genericRepository;

    @Autowired
    public AdminService(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(Role.values());
    }

    public ResponseEntity<?> getTopSeller(Pageable pageable) {
        return ResponseEntity.ok(genericRepository.getTopSeller(pageable));
    }

    public ResponseEntity<?> getBookStatus() {
        return ResponseEntity.ok(genericRepository.getBookStatus());
    }

    public ResponseEntity<?> getTodayFeatured() {
        return ResponseEntity.ok(genericRepository.getTodayFeatured());
    }

    public ResponseEntity<?> getRevenueByWeek() {
        return ResponseEntity.ok(genericRepository.getRevenueByWeek());
    }

    public ResponseEntity<?> getCustomerStatistical(Pageable pageable) {
        return ResponseEntity.ok(genericRepository.getCustomerStatistical(pageable));
    }

    public ResponseEntity<?> getCustomerStatisticalByRole(Role role, Pageable pageable) {
        return ResponseEntity.ok(genericRepository.getCustomerStatistical(role, pageable));

    }

    public ResponseEntity<?> getRevenueByMonth(int month, int year) {
        return ResponseEntity.ok(genericRepository.getRevenueByMonth(month, year));
    }

    public ResponseEntity<?> getRevenueByDate(LocalDate date) {
        return ResponseEntity.ok(genericRepository.getRevenueByDate(date));
    }

    public ResponseEntity<?> getRevenueByYear(int year) {
        return ResponseEntity.ok(genericRepository.getRevenueByYear(year));
    }

    public ResponseEntity<?> getRevenueAllTime() {
        return ResponseEntity.ok(genericRepository.getRevenueAllTime());
    }
}
