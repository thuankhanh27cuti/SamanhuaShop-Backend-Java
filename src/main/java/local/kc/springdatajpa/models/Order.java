package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "order_consignee_name", nullable = false)
    private String consigneeName;

    @Column(name = "order_address", nullable = false)
    private String address;

    @Column(name = "order_phone", nullable = false)
    private String phone;

    @Column(name = "order_created_at", nullable = false)
    private Date createAt;

    @Column(name = "order_finished_at")
    private Date finishedAt;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private Set<OrderDetail> orderDetails;

    public Order(Integer id, String consigneeName, String address, String phone, Date createAt, Date finishedAt, OrderStatus orderStatus) {
        this.id = id;
        this.consigneeName = consigneeName;
        this.address = address;
        this.phone = phone;
        this.createAt = createAt;
        this.finishedAt = finishedAt;
        this.orderStatus = orderStatus;
    }

    public Order(Integer id) {
        this.id = id;
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        if (this.orderDetails == null) {
            this.orderDetails = new HashSet<>();
        }
        this.orderDetails.add(orderDetail);
    }

    public void removeOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.remove(orderDetail);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", consigneeName='" + consigneeName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", createAt=" + createAt +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
