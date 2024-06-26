package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.dtos.ImageDTO;
import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.models.Image;
import local.kc.springdatajpa.repositories.v1.ImageRepository;
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

    public ResponseEntity<?> addImage(ImageDTO imageDTO) {
        Image image = Image.builder()
                .src(imageDTO.getSrc())
                .book(new Book(imageDTO.getBook().getId()))
                .build();
        imageRepository.save(image);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteImage(int id) {
        imageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
