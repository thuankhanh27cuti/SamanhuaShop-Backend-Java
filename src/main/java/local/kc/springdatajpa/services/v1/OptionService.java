package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.dtos.OptionDTO;
import local.kc.springdatajpa.models.Book;
import local.kc.springdatajpa.models.Option;
import local.kc.springdatajpa.repositories.v1.OptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

    public ResponseEntity<?> findByIdLazy(int id) {
        return ResponseEntity.of(optionRepository.findByIdLazy(id)
                .map(option -> modelMapper.map(option, OptionDTO.class)));
    }

    public ResponseEntity<?> findAllOptionsByBookId(int id) {
        return ResponseEntity.ok(optionRepository.findAllByBookId(id)
                .stream()
                .map(option -> modelMapper.map(option, OptionDTO.class))
                .toList());
    }

    public ResponseEntity<?> findOptionsByBookId(int id, Pageable pageable) {
        return ResponseEntity.ok(optionRepository.findByBookId(id, pageable)
                .stream()
                .map(option -> modelMapper.map(option, OptionDTO.class))
                .toList());
    }

    public ResponseEntity<?> countByBookId(int id) {
        return ResponseEntity.ok(optionRepository.countByBookId(id));
    }

    public ResponseEntity<?> saveOption(OptionDTO optionDTO) {
        Option option = Option.builder()
                .name(optionDTO.getName())
                .image(optionDTO.getImage())
                .quantity(optionDTO.getQuantity())
                .book(new Book(optionDTO.getBook().getId()))
                .build();

        return ResponseEntity.ok(modelMapper.map(optionRepository.save(option), OptionDTO.class));
    }

    public ResponseEntity<?> updateOption(int id, OptionDTO optionDTO) {
        Option option = optionRepository.findById(id).orElse(null);
        if (option == null) {
            return ResponseEntity.notFound().build();
        }

        option.setName(optionDTO.getName());
        option.setImage(optionDTO.getImage());
        option.setQuantity(optionDTO.getQuantity());
        option.setBook(new Book(optionDTO.getBook().getId()));
        optionRepository.save(option);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteOption(int id) {
        Option option = optionRepository.findById(id).orElse(null);
        if (option == null) {
            return ResponseEntity.notFound().build();
        }

        option.setDeleted(!option.isDeleted());
        optionRepository.save(option);

        return ResponseEntity.ok().build();
    }
}
