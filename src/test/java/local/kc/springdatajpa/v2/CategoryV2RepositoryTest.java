package local.kc.springdatajpa.v2;

import local.kc.springdatajpa.models.Category;
import local.kc.springdatajpa.repositories.v2.CategoryV2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CategoryV2RepositoryTest {

    @Autowired
    private CategoryV2Repository categoryV2Repository;

    @Test
    public void findByBookIdLazy() {
        List<Category> categories = categoryV2Repository.findByBookIdLazy(1);
        System.out.println(categories);
    }
}
