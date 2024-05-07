package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.CategoryDTO;
import local.kc.springdatajpa.repositories.CategoryRepository;
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

    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(categoryRepository.findAllLazy(pageable).stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList());
    }

    public ResponseEntity<?> findById(int id) {
        return categoryRepository.findByIdLazy(id)
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
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
}