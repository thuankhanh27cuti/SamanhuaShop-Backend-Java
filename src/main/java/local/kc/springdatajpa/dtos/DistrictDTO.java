package local.kc.springdatajpa.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDTO implements Serializable {
    Integer code;
    String name;
    String fullName;
    boolean isDeleted;
    Set<OrderDTO> orders;
    ProvinceDTO province;
    Set<WardDTO> wards;

    public DistrictDTO(Integer code) {
        this.code = code;
    }
}