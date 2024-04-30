package local.kc.springdatajpa.dtos;

import local.kc.springdatajpa.models.Role;
import lombok.*;

import java.io.Serializable;
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
}