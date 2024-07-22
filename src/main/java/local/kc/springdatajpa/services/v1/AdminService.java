package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.dtos.BookDTO;
import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.models.Option;
import local.kc.springdatajpa.models.OrderStatus;
import local.kc.springdatajpa.models.Role;
import local.kc.springdatajpa.repositories.v1.BookRepository;
import local.kc.springdatajpa.repositories.v1.GenericRepository;
import local.kc.springdatajpa.repositories.v1.OptionRepository;
import local.kc.springdatajpa.utils.OrderStatusRes;
import local.kc.springdatajpa.utils.RoleRes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class AdminService {
    private final GenericRepository genericRepository;
    private final BookRepository bookRepository;
    private final OptionRepository optionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminService(GenericRepository genericRepository, BookRepository bookRepository, OptionRepository optionRepository, ModelMapper modelMapper) {
        this.genericRepository = genericRepository;
        this.bookRepository = bookRepository;
        this.optionRepository = optionRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(Arrays.stream(Role.values()).map(role -> new RoleRes(role.getValue(), role.toString())));
    }

    public ResponseEntity<?> getAllOrderStates() {
        return ResponseEntity.ok(Arrays.stream(OrderStatus.values()).map(orderStatus -> new OrderStatusRes(orderStatus.getValue(), orderStatus.toString())));
    }

    public ResponseEntity<?> getTopSeller(Pageable pageable) {
        return ResponseEntity.ok(genericRepository.getTopSeller(pageable));
    }

    public ResponseEntity<?> getBookStatus() {
        return ResponseEntity.ok(genericRepository.getBookStatus());
    }

    public ResponseEntity<?> getTodayFeatured() {
        return ResponseEntity.ok(genericRepository.getTodayFeatured());
    }

    public ResponseEntity<?> getRevenueByWeek() {
        return ResponseEntity.ok(genericRepository.getRevenueByWeek());
    }

    public ResponseEntity<?> getRevenueByMonth(int month, int year) {
        return ResponseEntity.ok(genericRepository.getRevenueByMonth(month, year));
    }

    public ResponseEntity<?> getRevenueByDate(LocalDate date) {
        return ResponseEntity.ok(genericRepository.getRevenueByDate(date));
    }

    public ResponseEntity<?> getRevenueByYear(int year) {
        return ResponseEntity.ok(genericRepository.getRevenueByYear(year));
    }

    public ResponseEntity<?> getRevenueAllTime() {
        return ResponseEntity.ok(genericRepository.getRevenueAllTime());
    }

    public ResponseEntity<?> getOptionOutOfStock(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "quantity");
        List<Option> options = optionRepository.findAllOptionQuantityAsc(PageRequest.of(pageNumber, pageSize, sort));

        return ResponseEntity.ok(options.stream()
                .map(option -> {
                    Book book = bookRepository.findByOptionId(option.getId()).orElse(null);
                    if (book != null) {
                        book.setOptions(Set.of(option));
                    }
                    return book;
                })
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList());
    }

    public ResponseEntity<?> getStatisticalRevenueByDate(LocalDate date) {
        return ResponseEntity.ok(genericRepository.getStatisticalRevenueByDate(date));

    }

    public ResponseEntity<?> getStatisticalRevenueByMonth(int month, int year) {
        return ResponseEntity.ok(genericRepository.getStatisticalRevenueByMonth(month, year));

    }

    public ResponseEntity<?> getStatisticalRevenueByYear(int year) {
        return ResponseEntity.ok(genericRepository.getStatisticalRevenueByYear(year));
    }

    public ResponseEntity<?> getStatisticalRevenueAllTime() {
        return ResponseEntity.ok(genericRepository.getStatisticalRevenueAllTime());
    }

    public ResponseEntity<?> getCountOptionOutOfStock() {
        return ResponseEntity.ok(optionRepository.countByQuantityLessThanEqual(10));
    }

    public ResponseEntity<?> getCountValuableCustomer() {
        return ResponseEntity.ok(genericRepository.getCountValuableCustomer());
    }

    public ResponseEntity<?> getStatisticalRevenueByProvince() {
        return ResponseEntity.ok(genericRepository.getStatisticalRevenueByProvince());
    }

    public ResponseEntity<?> getStatisticalRevenueByDistrict(int provinceId) {
        return ResponseEntity.ok(genericRepository.getStatisticalRevenueByDistrict(provinceId));
    }

    public ResponseEntity<?> getStatisticalRevenueByWard(int districtId) {
        return ResponseEntity.ok(genericRepository.getStatisticalRevenueByWard(districtId));
    }

    public ResponseEntity<?> getChartUserProvince() {
        return ResponseEntity.ok(genericRepository.getChartUserProvince());
    }

    public ResponseEntity<?> getChartUserDistrict(int provinceId) {
        return ResponseEntity.ok(genericRepository.getChartUserDistrict(provinceId));
    }

    public ResponseEntity<?> getChartUserWard(int districtId) {
        return ResponseEntity.ok(genericRepository.getChartUserWard(districtId));
    }

    public ResponseEntity<?> getChartRevenueByCategory() {
        return ResponseEntity.ok(genericRepository.getChartRevenueByCategory());
    }
}
