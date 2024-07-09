package local.kc.springdatajpa.v1;

import local.kc.springdatajpa.repositories.v1.GenericRepository;
import local.kc.springdatajpa.utils.statistical.StatisticalByDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class GenericRepositoryTest {

    @Autowired
    private GenericRepository genericRepository;

    @Test
    void getStatisticalRevenueByDate() {
        LocalDate localDate = LocalDate.now();
        List<StatisticalByDate> statisticalRevenueByDate = genericRepository.getStatisticalRevenueByDate(localDate);
        System.out.println(statisticalRevenueByDate);
    }
}
