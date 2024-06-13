package local.kc.springdatajpa.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO implements Serializable {
    Integer id;
    String src;
    BookDTO book;

    public ImageDTO(String src) {
        this.src = src;
    }
}