package local.kc.springdatajpa.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderLogDTO implements Serializable {
    Integer id;
    String description;
    Date time;
    OrderDTO order;
}