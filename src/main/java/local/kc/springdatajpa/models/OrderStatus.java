package local.kc.springdatajpa.models;

import lombok.Getter;

@Getter
public enum OrderStatus {
    WAIT_FOR_PAY(0),
    PENDING(1),
    PREPARING(2),
    SHIPPING(3),
    SUCCESS(4),
    DECLINED(5);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

}
