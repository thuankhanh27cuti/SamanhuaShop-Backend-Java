package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id) {
        return categoryService.findById(id);
    }

    @GetMapping("/by-book/{id}")
    public ResponseEntity<?> findByBookId(@PathVariable(name = "id") int id) {
        return categoryService.findByBookId(id);
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        return categoryService.count();
    }
}
