package local.kc.springdatajpa.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class OrderDetailId implements Serializable {
    private Integer orderId;
    private Integer optionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailId that = (OrderDetailId) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(optionId, that.optionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, optionId);
    }

    @Override
    public String toString() {
        return "OrderDetailId{" +
                "orderId=" + orderId +
                ", optionId=" + optionId +
                '}';
    }
}