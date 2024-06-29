package local.kc.springdatajpa.dtos;

import local.kc.springdatajpa.converters.RoleConverter;
import local.kc.springdatajpa.models.Role;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO implements Serializable {
    Integer id;
    String name;
    String gender;
    String image;
    String phone;
    String username;
    String password;
    Role role;
    Set<OrderDTO> orders;
    LocalDate birthday;
    boolean isDeleted;
    WardDTO ward;

    public CustomerDTO(Integer id) {
        this.id = id;
    }

    public CustomerDTO(Integer id, String name, String gender, String image, String phone, String username, String password, int role, Set<OrderDTO> orders, LocalDate birthday, boolean isDeleted, WardDTO ward) {
        RoleConverter roleConverter = new RoleConverter();

        this.id = id;
        this.name = name;
        this.gender = gender;
        this.image = image;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.role = roleConverter.convertToEntityAttribute(role);
        this.orders = orders;
        this.birthday = birthday;
        this.isDeleted = isDeleted;
        this.ward = ward;
    }
}