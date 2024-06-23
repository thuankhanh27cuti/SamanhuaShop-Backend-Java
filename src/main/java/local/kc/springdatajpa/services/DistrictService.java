package local.kc.springdatajpa.services;

import local.kc.springdatajpa.repositories.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public ResponseEntity<?> findByProvinceId(int id) {
        return ResponseEntity.ok(districtRepository.findByProvinceId(id));
    }
}
