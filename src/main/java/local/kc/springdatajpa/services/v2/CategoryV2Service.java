package local.kc.springdatajpa.services.v2;

import local.kc.springdatajpa.dtos.CategoryDTO;
import local.kc.springdatajpa.repositories.v2.CategoryV2Repository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryV2Service {
    private final CategoryV2Repository categoryV2Repository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryV2Service(CategoryV2Repository categoryV2Repository, ModelMapper modelMapper) {
        this.categoryV2Repository = categoryV2Repository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> getCategories(Pageable pageable) {
        return ResponseEntity.ok(categoryV2Repository.findAllLazy(pageable).stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList());
    }

    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryV2Repository.findAllLazy().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList());
    }

    public ResponseEntity<?> findByBookId(int id) {
        return ResponseEntity.ok(categoryV2Repository.findByBookIdLazy(id).stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList());
    }
}
