package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.dtos.CategoryDTO;
import local.kc.springdatajpa.models.Category;
import local.kc.springdatajpa.repositories.v1.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository,
                           ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(categoryRepository.findAllLazy().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList());
    }


    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(categoryRepository.findAllLazy(pageable).stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList());
    }

    public ResponseEntity<?> findById(int id) {
        return ResponseEntity.of(categoryRepository.findByIdLazy(id)
                .map(category -> modelMapper.map(category, CategoryDTO.class)));
    }

    public ResponseEntity<?> findByBookId(int id) {
        return ResponseEntity.ok(categoryRepository.findByBookIdLazy(id).stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList());
    }

    public ResponseEntity<?> count() {
        long count = categoryRepository.count();
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<?> saveCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .image(categoryDTO.getImage())
                .build();
        categoryRepository.save(category);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> updateCategory(int id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        category.setName(categoryDTO.getName());
        category.setImage(categoryDTO.getImage());
        categoryRepository.save(category);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        category.setDeleted(!category.isDeleted());
        categoryRepository.save(category);
        return ResponseEntity.ok().build();
    }
}
