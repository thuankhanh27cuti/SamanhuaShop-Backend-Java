package local.kc.springdatajpa.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WardDTO implements Serializable {
    Integer code;
    String name;
    String fullName;
    boolean isDeleted;
    DistrictDTO district;
    Set<OrderDTO> orders;
    Set<CustomerDTO> customers;

    public WardDTO(Integer code) {
        this.code = code;
    }
}