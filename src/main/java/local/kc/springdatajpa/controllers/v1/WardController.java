package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.services.v1.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wards")
public class WardController {
    private final WardService wardService;

    @Autowired
    public WardController(WardService wardService) {
        this.wardService = wardService;
    }
    
    @GetMapping("/by-district/{id}")
    public ResponseEntity<?> findByDistrictId(@PathVariable(name = "id") int id) {
        return wardService.findByDistrictId(id);
    }
}
