package local.kc.springdatajpa.controllers.v2;

import local.kc.springdatajpa.services.v2.BookV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/books")
public class BookV2Controller {
    private final BookV2Service bookService;

    @Autowired
    public BookV2Controller(BookV2Service bookV2Service) {
        this.bookService = bookV2Service;
    }

    @GetMapping
    public ResponseEntity<?> getBooks(Pageable pageable) {
        return bookService.getBooks(pageable);
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

}
