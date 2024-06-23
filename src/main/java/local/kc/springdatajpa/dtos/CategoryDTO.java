package local.kc.springdatajpa.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {
    Integer id;
    String name;
    String image;
    Set<BookDTO> books;
    boolean isDeleted;

    public CategoryDTO(Integer id) {
        this.id = id;
    }
}