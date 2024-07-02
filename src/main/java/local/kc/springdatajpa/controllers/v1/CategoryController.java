package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.dtos.CategoryDTO;
import local.kc.springdatajpa.services.v1.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return categoryService.findAll();
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

    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.saveCategory(categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable(name = "id") int id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "id") int id) {
        return categoryService.deleteCategory(id);
    }
}
