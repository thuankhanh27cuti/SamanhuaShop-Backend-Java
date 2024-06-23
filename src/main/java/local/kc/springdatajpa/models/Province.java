package local.kc.springdatajpa.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "provinces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Province {
    @Id
    @Column(name = "province_code")
    private Integer code;

    @Column(name = "province_name", nullable = false)
    private String name;

    @Column(name = "province_full_name", nullable = false)
    private String fullName;

    @Column(name = "province_is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "province")
    private Set<Order> orders;

    @OneToMany(mappedBy = "province", cascade = CascadeType.REMOVE)
    private Set<District> districts;

    public Province(Integer code, String name, String fullName) {
        this.code = code;
        this.name = name;
        this.fullName = fullName;
    }
}
