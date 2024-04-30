package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/options")
public class OptionController {
    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/by-book/{id}")
    public ResponseEntity<?> findOptionsByBookId(@PathVariable(name = "id") int id) {
        return optionService.findOptionsByBookId(id);
    }

}
