package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.services.v1.Base64Service;
import local.kc.springdatajpa.utils.base64.Base64Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/file")
public class Base64Controller {
    private final Base64Service base64Service;

    @Autowired
    public Base64Controller(Base64Service base64Service) {
        this.base64Service = base64Service;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestBody Base64Request base64Request) {
        return base64Service.uploadFile(base64Request);
    }

    @GetMapping("/images/{url}")
    public ResponseEntity<?> getImage(@PathVariable(name = "url") String url) {
        return base64Service.getImage(url);
    }
}
