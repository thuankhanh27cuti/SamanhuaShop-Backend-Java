package local.kc.springdatajpa.services;

import local.kc.springdatajpa.repositories.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProvinceService {
    private final ProvinceRepository provinceRepository;

    @Autowired
    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(provinceRepository.findAllLazy());
    }
}
