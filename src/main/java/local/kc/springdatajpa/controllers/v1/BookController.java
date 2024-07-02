package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.dtos.BookDTO;
import local.kc.springdatajpa.services.v1.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id) {
        return bookService.findById(id);
    }

    @GetMapping("/by-category/{id}")
    public ResponseEntity<?> findByCategoryId(Pageable pageable, @PathVariable(name = "id") int categoryId) {
        return bookService.findByCategoryId(categoryId, pageable);
    }

    @GetMapping("/by-option/{id}")
    public ResponseEntity<?> findByOptionId(@PathVariable(name = "id") int optionId) {
        return bookService.findByOptionId(optionId);
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        return bookService.count();
    }

    @GetMapping("/count/by-category/{id}")
    public ResponseEntity<?> countByCategoryId(@PathVariable(name = "id") int categoryId) {
        return bookService.countByCategoryId(categoryId);
    }

    @PostMapping
    public ResponseEntity<?> saveBook(@RequestBody BookDTO bookDTO) {
        return bookService.saveBook(bookDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable(name = "id") int id, @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable(name = "id") int id) {
        return bookService.deleteBook(id);
    }
}
