package local.kc.springdatajpa.services;

import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.repositories.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
