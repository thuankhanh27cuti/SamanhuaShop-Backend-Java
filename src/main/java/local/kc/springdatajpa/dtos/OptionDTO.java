package local.kc.springdatajpa.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OptionDTO implements Serializable {
    Integer id;
    String name;
    Integer quantity;
    String image;
    BookDTO book;
    Set<OrderDetailDTO> ordersDetails;
}