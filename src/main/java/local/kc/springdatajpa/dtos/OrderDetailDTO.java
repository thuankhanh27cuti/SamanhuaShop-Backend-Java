package local.kc.springdatajpa.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO implements Serializable {
    OrderDTO order;
    OptionDTO option;
    Integer quantity;
    Float price;
}