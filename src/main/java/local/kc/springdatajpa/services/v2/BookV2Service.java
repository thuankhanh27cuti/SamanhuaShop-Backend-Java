package local.kc.springdatajpa.services.v2;

import local.kc.springdatajpa.dtos.BookDTO;
import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.models.Option;
import local.kc.springdatajpa.repositories.v2.BookV2Repository;
import local.kc.springdatajpa.repositories.v2.OptionV2Repository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookV2Service {
    private final BookV2Repository bookRepository;
    private final ModelMapper modelMapper;
    private final OptionV2Repository optionV2Repository;

    @Autowired
    public BookV2Service(BookV2Repository bookRepository, ModelMapper modelMapper, OptionV2Repository optionV2Repository) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.optionV2Repository = optionV2Repository;
    }

    public ResponseEntity<?> getBooks(Pageable pageable) {
        return ResponseEntity.ok(bookRepository.findAllLazy(pageable).stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList()));
    }

    public ResponseEntity<?> findByCategoryId(int categoryId, Pageable pageable) {
        return ResponseEntity.ok(bookRepository.findByCategoryIdLazy(categoryId, pageable).stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList()));
    }

    public ResponseEntity<?> findById(int id) {
        return ResponseEntity.of(bookRepository.findByIdLazy(id));
    }

    public ResponseEntity<?> count() {
        long count = bookRepository.count();
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<?> countByCategoryId(int id) {
        long count = bookRepository.countByCategoryId(id);
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<?> findByOptionId(int optionId) {
        Option option = optionV2Repository.findByIdLazy(optionId).orElse(null);
        Book book = bookRepository.findByOptionId(optionId).orElse(null);

        if (option == null || book == null) {
            return ResponseEntity.notFound().build();
        }

        book.setCategories(new HashSet<>());
        book.setOptions(new HashSet<>());
        book.setOptions(Set.of(option));

        return ResponseEntity.ok(book);
    }
}
