package local.kc.springdatajpa.services;

import local.kc.springdatajpa.dtos.OptionDTO;
import local.kc.springdatajpa.repositories.OptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OptionService(OptionRepository optionRepository,
                         ModelMapper modelMapper) {
        this.optionRepository = optionRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> findOptionsByBookId(int id) {
        return ResponseEntity.ok(optionRepository.findByBookId(id)
                .stream()
                .map(option -> modelMapper.map(option, OptionDTO.class))
                .toList());
    }
}
