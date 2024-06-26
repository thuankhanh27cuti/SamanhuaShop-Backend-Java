package local.kc.springdatajpa.services.v2;

import local.kc.springdatajpa.dtos.OptionDTO;
import local.kc.springdatajpa.repositories.v2.OptionV2Repository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OptionV2Service {
    private final OptionV2Repository optionV2Repository;
    private final ModelMapper modelMapper;

    @Autowired
    public OptionV2Service(OptionV2Repository optionV2Repository, ModelMapper modelMapper) {
        this.optionV2Repository = optionV2Repository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> findOptionsByBookId(int id) {
        return ResponseEntity.ok(optionV2Repository.findByBookId(id)
                .stream()
                .map(option -> modelMapper.map(option, OptionDTO.class))
                .toList());
    }
}
