package local.kc.springdatajpa.services.v2;

import local.kc.springdatajpa.dtos.CustomerDTO;
import local.kc.springdatajpa.models.Customer;
import local.kc.springdatajpa.models.District;
import local.kc.springdatajpa.models.Province;
import local.kc.springdatajpa.models.Ward;
import local.kc.springdatajpa.repositories.v1.DistrictRepository;
import local.kc.springdatajpa.repositories.v1.ProvinceRepository;
import local.kc.springdatajpa.repositories.v1.WardRepository;
import local.kc.springdatajpa.repositories.v2.CustomerV2Repository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerV2Service {
    private final CustomerV2Repository customerRepository;
    private final WardRepository wardRepository;
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerV2Service(CustomerV2Repository customerRepository, WardRepository wardRepository,
                             DistrictRepository districtRepository,
                             ProvinceRepository provinceRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.wardRepository = wardRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> findById(int id) {
        Customer customer = customerRepository.findByIdLazy(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        Ward ward = wardRepository.findByCustomerId(id).map(w -> Ward.builder()
                .code(w.getCode())
                .name(w.getName())
                .fullName(w.getFullName())
                .district(districtRepository.findByWardId(w.getCode()).map(d -> District.builder()
                        .code(d.getCode())
                        .name(d.getName())
                        .fullName(d.getFullName())
                        .province(provinceRepository.findByDistrictId(d.getCode()).map(p -> Province.builder()
                                .code(p.getCode())
                                .name(p.getName())
                                .fullName(p.getFullName())
                                .build()).orElse(null))
                        .build()).orElse(null))
                .build()).orElse(null);

        customer.setWard(ward);
        return ResponseEntity.ok(modelMapper.map(customer, CustomerDTO.class));
    }

    public ResponseEntity<?> updateCustomer(int id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        customer.setName(customerDTO.getName());
        customer.setGender(customerDTO.getGender());
        customer.setImage(customerDTO.getImage());
        customer.setPhone(customerDTO.getPhone());
        customer.setBirthday(customerDTO.getBirthday());
        if (customerDTO.getWard() != null) {
            customer.setWard(new Ward(customerDTO.getWard().getCode()));
        }
        else {
            customer.setWard(null);
        }
        customerRepository.save(customer);

        return ResponseEntity.ok().build();
    }
}
