package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.dtos.ImageDTO;
import local.kc.springdatajpa.services.v1.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/by-book/{id}")
    public ResponseEntity<?> getImagesByBookId(@PathVariable(name = "id") int id) {
        return imageService.getImagesByBookId(id);
    }

    @PostMapping()
    public ResponseEntity<?> addImage(@RequestBody ImageDTO imageDTO) {
        return imageService.addImage(imageDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "id") int id) {
        return imageService.deleteImage(id);
    }
}
