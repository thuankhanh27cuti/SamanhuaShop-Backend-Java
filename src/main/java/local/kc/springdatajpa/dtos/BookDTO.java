package local.kc.springdatajpa.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import local.kc.springdatajpa.serializations.BookDTOSerialization;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = BookDTOSerialization.class)
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
}