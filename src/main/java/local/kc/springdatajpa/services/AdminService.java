package local.kc.springdatajpa.services;

import local.kc.springdatajpa.repositories.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(genericRepository.getAllRoles());
    }
}
