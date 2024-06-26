package local.kc.springdatajpa.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO implements Serializable {
    Integer id;
    String name;
    String image;
    Integer price;
    String description;
    Date createAt;
    Set<CategoryDTO> categories;
    Set<OptionDTO> options;
    Set<ImageDTO> images;
    boolean isDeleted;

    public BookDTO(Integer id) {
        this.id = id;
    }
}