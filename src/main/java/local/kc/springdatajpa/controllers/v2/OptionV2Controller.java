package local.kc.springdatajpa.controllers.v2;

import local.kc.springdatajpa.services.v2.OptionV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/options")
public class OptionV2Controller {
    private final OptionV2Service optionV2Service;

    @Autowired
    public OptionV2Controller(OptionV2Service optionV2Service) {
        this.optionV2Service = optionV2Service;
    }

    @GetMapping("/by-book/{id}")
    public ResponseEntity<?> findOptionsByBookId(@PathVariable(name = "id") int id) {
        return optionV2Service.findOptionsByBookId(id);
    }
}
