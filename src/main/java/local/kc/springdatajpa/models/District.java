package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "districts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District {
    @Id
    @Column(name = "district_code")
    private Integer code;

    @Column(name = "district_name", nullable = false)
    private String name;

    @Column(name = "district_full_name", nullable = false)
    private String fullName;

    @Column(name = "district_is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "district")
    private Set<Order> orders;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @OneToMany(mappedBy = "district", cascade = CascadeType.REMOVE)
    private Set<Ward> wards;

    public District(Integer code, String name, String fullName) {
        this.code = code;
        this.name = name;
        this.fullName = fullName;
    }
}
