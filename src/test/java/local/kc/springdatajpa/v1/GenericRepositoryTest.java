package local.kc.springdatajpa.v1;

import local.kc.springdatajpa.repositories.v1.GenericRepository;
import local.kc.springdatajpa.utils.statistical.StatisticalByCustomerProvince;
import local.kc.springdatajpa.utils.statistical.StatisticalByDate;
import local.kc.springdatajpa.utils.statistical.StatisticalByWard;
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

    @Test
    void getStatisticalRevenueByWard() {
        List<StatisticalByWard> statisticalRevenueByWard = genericRepository.getStatisticalRevenueByWard(282);
        System.out.println(statisticalRevenueByWard);
    }

    @Test
    void getChartUserProvince() {
        List<StatisticalByCustomerProvince> list = genericRepository.getChartUserProvince();
        list.forEach(System.out::println);
    }
}
