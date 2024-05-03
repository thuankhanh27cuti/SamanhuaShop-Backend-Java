package local.kc.springdatajpa;

import local.kc.springdatajpa.repositories.GenericRepository;
import local.kc.springdatajpa.utils.BookStatus;
import local.kc.springdatajpa.utils.RevenueByDate;
import local.kc.springdatajpa.utils.TopSellerBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
public class GenericRepositoryTest {

    @Autowired
    private GenericRepository genericRepository;

    @Test
    void getTopSeller() {
        Sort.Order order = Sort.Order.desc("quantity");
        Sort.Order order1 = Sort.Order.desc("revenue");
        Sort sort = Sort.by(order, order1);
        Pageable pageable = PageRequest.of(0, 5, sort);
        List<TopSellerBook> topSeller = genericRepository.getTopSeller(pageable);
        topSeller.forEach(System.out::println);
    }

    @Test
    void getProductStatus() {
        List<BookStatus> bookStatusList = genericRepository.getBookStatus();
        bookStatusList.forEach(System.out::println);
    }

    @Test
    void getRevenueByWeek() {
        List<RevenueByDate> revenueByWeek = genericRepository.getRevenueByWeek();
        System.out.println(revenueByWeek);
    }
}
