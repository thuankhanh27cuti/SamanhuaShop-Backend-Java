package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.repositories.v1.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WardService {
    private final WardRepository wardRepository;

    @Autowired
    public WardService(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    public ResponseEntity<?> findByDistrictId(int id) {
        return ResponseEntity.ok(wardRepository.findByDistrictId(id));
    }
}
