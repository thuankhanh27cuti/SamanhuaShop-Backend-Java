package local.kc.springdatajpa.controllers.v2;

import local.kc.springdatajpa.services.v2.CategoryV2Service;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/categories")
public class CategoryV2Controller {
    private final CategoryV2Service categoryV2Service;

    public CategoryV2Controller(CategoryV2Service categoryV2Service) {
        this.categoryV2Service = categoryV2Service;
    }

    @GetMapping
    public ResponseEntity<?> getCategories(Pageable pageable) {
        return categoryV2Service.getCategories(pageable);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
        return categoryV2Service.getAllCategories();
    }

    @GetMapping("/by-book/{id}")
    public ResponseEntity<?> findByBookId(@PathVariable(name = "id") int id) {
        return categoryV2Service.findByBookId(id);
    }
}
