package local.kc.springdatajpa.services;

import local.kc.springdatajpa.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public ResponseEntity<?> getImagesByBookId(int id) {
        return ResponseEntity.ok(imageRepository.findByBookId(id));
    }
}
