package local.kc.springdatajpa.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDTO implements Serializable {
    Integer code;
    String name;
    String fullName;
    boolean isDeleted;
    Set<OrderDTO> orders;
    Set<DistrictDTO> districts;

    public ProvinceDTO(Integer code) {
        this.code = code;
    }
}