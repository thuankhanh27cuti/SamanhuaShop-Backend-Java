package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.BookDTO;
import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.repositories.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> findAll(Pageable pageable) {
        List<Book> books = bookRepository.findAllLazy(pageable);
        List<BookDTO> bookDTOS = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookDTOS);
    }

    public ResponseEntity<?> findById(int id) {
        return bookRepository.findByIdLazy(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> findByCategoryId(int categoryId, Pageable pageable) {
        List<Book> books = bookRepository.findByCategoryIdLazy(categoryId, pageable);
        List<BookDTO> bookDTOS = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookDTOS);
    }


    public ResponseEntity<?> findByOptionId(int optionId) {
        return bookRepository.findByOptionId(optionId)
                .map(book -> {
                            book.setCategories(new HashSet<>());
                            book.setImages(new HashSet<>());
                            book.setOptions(book.getOptions().stream().peek(option -> option.setOrdersDetails(new HashSet<>())).collect(Collectors.toSet()));
                            return book;
                        }
                )
                .map(book -> modelMapper.map(book, BookDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> count() {
        long count = bookRepository.count();
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<?> countByCategoryId(int id) {
        long count = bookRepository.countByCategoryId(id);
        return ResponseEntity.ok(count);
    }
}