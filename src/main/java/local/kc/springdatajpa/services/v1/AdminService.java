package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.models.OrderStatus;
import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.repositories.v1.GenericRepository;
import local.kc.springdatajpa.utils.OrderStatusRes;
import local.kc.springdatajpa.utils.RoleRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;

@Service
public class AdminService {
    private final GenericRepository genericRepository;

    @Autowired
    public AdminService(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(Arrays.stream(Role.values()).map(role -> new RoleRes(role.getValue(), role.toString())));
    }

    public ResponseEntity<?> getAllOrderStates() {
        return ResponseEntity.ok(Arrays.stream(OrderStatus.values()).map(orderStatus -> new OrderStatusRes(orderStatus.getValue(), orderStatus.toString())));
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
