package local.kc.springdatajpa.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "order_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_log_id")
    private Integer id;

    @Column(name = "order_log_description", nullable = false)
    private String description;

    @Column(name = "order_log_time", nullable = false)
    private Date time;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderLog(String description, Date time) {
        this.description = description;
        this.time = time;
    }

    @Override
    public String toString() {
        return "OrderLog{" +
                "description='" + description + '\'' +
                ", time=" + time +
                '}';
    }
}
