package local.kc.springdatajpa.controllers;

import local.kc.springdatajpa.models.Image;
import local.kc.springdatajpa.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
