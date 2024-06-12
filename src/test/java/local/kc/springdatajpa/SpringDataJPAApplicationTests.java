package local.kc.springdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class SpringDataJPAApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void generateRandomNumberString() {
        int length = 10;
        StringBuilder name = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int i1 = random.nextInt(10);
            name.append(i1);
        }

        System.out.println(name);
    }

}
