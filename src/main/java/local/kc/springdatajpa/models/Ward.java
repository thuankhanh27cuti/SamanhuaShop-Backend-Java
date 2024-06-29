package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "wards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ward {
    @Id
    @Column(name = "ward_code")
    private Integer code;

    @Column(name = "ward_name", nullable = false)
    private String name;

    @Column(name = "ward_full_name", nullable = false)
    private String fullName;

    @Column(name = "ward_is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @OneToMany(mappedBy = "ward")
    private Set<Order> orders;

    @OneToMany(mappedBy = "ward")
    private Set<Customer> customers;

    public Ward(Integer code) {
        this.code = code;
    }

    public Ward(Integer code, String name, String fullName) {
        this.code = code;
        this.name = name;
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Ward{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
