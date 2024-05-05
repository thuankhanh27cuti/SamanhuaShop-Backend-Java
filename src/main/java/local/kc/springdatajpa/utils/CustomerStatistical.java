package local.kc.springdatajpa.utils;

import local.kc.springdatajpa.models.Role;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerStatistical {
    public int id;
    public String gender;
    public String image;
    public String fullName;
    public String phone;
    public Role role;
    public String username;
    public int orderRemain;
    public int orderShipping;
    public int orderSuccess;
    public int countOrder;
    public Date lastPending;
    public Float totalPrice;
}
