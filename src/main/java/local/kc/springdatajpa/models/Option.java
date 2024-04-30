package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public void addOrderDetail(OrderDetail orderDetail) {
        if (ordersDetails == null) {
            ordersDetails = new HashSet<>();
        }
        ordersDetails.add(orderDetail);
    }
}
