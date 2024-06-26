package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.services.v1.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/districts")
public class DistrictController {
    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping("/by-province/{id}")
    public ResponseEntity<?> findByProvinceId(@PathVariable(name = "id") int id) {
        return districtService.findByProvinceId(id);
    }
}
