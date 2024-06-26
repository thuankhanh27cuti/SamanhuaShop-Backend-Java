package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.dtos.OptionDTO;
import local.kc.springdatajpa.services.v1.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/options")
public class OptionController {
    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id) {
        return optionService.findByIdLazy(id);
    }

    @GetMapping("/by-book/{id}")
    public ResponseEntity<?> findOptionsByBookId(@PathVariable(name = "id") int id, Pageable pageable) {
        return optionService.findOptionsByBookId(id, pageable);
    }

    @GetMapping("/all-by-book/{id}")
    public ResponseEntity<?> findAllOptionsByBookId(@PathVariable(name = "id") int id) {
        return optionService.findAllOptionsByBookId(id);
    }

    @GetMapping("/count/by-book/{id}")
    public ResponseEntity<?> countByBookId(@PathVariable(name = "id") int id) {
        return optionService.countByBookId(id);
    }

    @PostMapping
    public ResponseEntity<?> saveOption(@RequestBody OptionDTO optionDTO) {
        return optionService.saveOption(optionDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOption(@PathVariable(name = "id") int id, @RequestBody OptionDTO optionDTO) {
        return optionService.updateOption(id, optionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable(name = "id") int id) {
        return optionService.deleteOption(id);
    }
}
