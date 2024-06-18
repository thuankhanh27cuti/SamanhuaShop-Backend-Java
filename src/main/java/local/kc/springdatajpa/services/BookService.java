package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.BookDTO;
import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.models.Category;
import local.kc.springdatajpa.repositories.BookRepository;
import local.kc.springdatajpa.repositories.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookService(BookRepository bookRepository, ImageRepository imageRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.imageRepository = imageRepository;
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
        return ResponseEntity.of(bookRepository.findByIdLazy(id)
                .map(book -> modelMapper.map(book, BookDTO.class)));
    }

    public ResponseEntity<?> findByCategoryId(int categoryId, Pageable pageable) {
        List<Book> books = bookRepository.findByCategoryIdLazy(categoryId, pageable);
        List<BookDTO> bookDTOS = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookDTOS);
    }


    public ResponseEntity<?> findByOptionId(int optionId) {
        return ResponseEntity.of(bookRepository.findByOptionId(optionId)
                .map(book -> {
                            book.setCategories(new HashSet<>());
                            book.setImages(new HashSet<>());
                            book.setOptions(book.getOptions().stream().peek(option -> option.setOrdersDetails(new HashSet<>())).collect(Collectors.toSet()));
                            return book;
                        }
                )
                .map(book -> modelMapper.map(book, BookDTO.class)));
    }

    public ResponseEntity<?> count() {
        long count = bookRepository.count();
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<?> countByCategoryId(int id) {
        long count = bookRepository.countByCategoryId(id);
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<?> saveBook(BookDTO bookDTO) {
        Book b = modelMapper.map(bookDTO, Book.class);
        b.setCreateAt(new Date());

        Book book = bookRepository.save(b);

        b.getImages().forEach(image -> {
            image.setBook(new Book(book.getId()));
            imageRepository.save(image);
        });

        return ResponseEntity.ok(modelMapper.map(new Book(book.getId()), BookDTO.class));
    }

    public ResponseEntity<?> editBook(int id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        book.setName(bookDTO.getName());
        book.setImage(bookDTO.getImage());
        book.setDescription(bookDTO.getDescription());
        book.setPrice(bookDTO.getPrice());

        Book mapped = modelMapper.map(bookDTO, Book.class);
        Set<Category> categories = mapped.getCategories();
        book.setCategories(categories);

        bookRepository.save(book);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteBook(int id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> findByName(String name) {
        List<Book> books = bookRepository.findByNameLikeIgnoreCase(name);
        return ResponseEntity.ok(books);
    }
}
