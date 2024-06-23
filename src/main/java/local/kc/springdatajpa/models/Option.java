package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Integer id;

    @Column(name = "option_name", nullable = false)
    private String name;

    @Column(name = "option_quantity", nullable = false)
    private Integer quantity;

    @Column(name = "option_image", nullable = false)
    private String image;

    @Column(name = "option_is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToMany(mappedBy = "option", cascade = CascadeType.REMOVE)
    private Set<OrderDetail> ordersDetails;

    public Option(Integer id) {
        this.id = id;
    }

    public Option(Integer id, String name, Integer quantity, String image) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", image='" + image + '\'' +
                '}';
    }
}
