package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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

    @Column(name = "order_address_detail", nullable = false)
    private String address;

    @Column(name = "order_phone", nullable = false)
    private String phone;

    @Column(name = "order_created_at", nullable = false)
    private Date createAt;

    @Column(name = "order_finished_at")
    private Date finishedAt;

    @Column(name = "order_payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private Set<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private Set<OrderLog> orderLogs;

    public Order(Integer id, PaymentMethod paymentMethod) {
        this.id = id;
        this.paymentMethod = paymentMethod;
    }

    public Order(Integer id, String consigneeName, String address, String phone, Date createAt, Date finishedAt, OrderStatus orderStatus) {
        this.id = id;
        this.consigneeName = consigneeName;
        this.address = address;
        this.phone = phone;
        this.createAt = createAt;
        this.finishedAt = finishedAt;
        this.orderStatus = orderStatus;
    }

    public Order(Integer id, String consigneeName, String address, String phone, Date createAt, Date finishedAt, PaymentMethod paymentMethod, OrderStatus orderStatus) {
        this.id = id;
        this.consigneeName = consigneeName;
        this.address = address;
        this.phone = phone;
        this.createAt = createAt;
        this.finishedAt = finishedAt;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }

    public Order(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", consigneeName='" + consigneeName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", createAt=" + createAt +
                ", finishedAt=" + finishedAt +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
