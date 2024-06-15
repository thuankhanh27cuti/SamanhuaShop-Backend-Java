package local.kc.springdatajpa;

import local.kc.springdatajpa.models.Image;
import local.kc.springdatajpa.repositories.ImageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Test
    void findByBookId() {
        List<Image> images = imageRepository.findByBookId(1);
        System.out.println(images);
    }
}
