package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer id;

    @Column(name = "customer_full_name", nullable = false)
    private String name;

    @Column(name = "customer_gender", nullable = false)
    private String gender;

    @Column(name = "customer_image")
    private String image;

    @Column(name = "customer_phone", nullable = false)
    private String phone;

    @Column(name = "customer_username", nullable = false, unique = true)
    private String username;

    @Column(name = "customer_password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;

    @Column(name = "customer_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public Customer(Integer id, String name, String gender, String image, String phone, String username, Role role) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.image = image;
        this.phone = phone;
        this.username = username;
        this.role = role;
    }

    public Customer(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", image='" + image + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addOrder(Order order) {
        if (orders == null) {
            orders = new HashSet<>();
        }
        orders.add(order);
    }
}