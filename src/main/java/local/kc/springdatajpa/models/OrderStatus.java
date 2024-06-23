package local.kc.springdatajpa.models;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING(0),
    PREPARING(1),
    SHIPPING(2),
    SUCCESS(3),
    DECLINED(4);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

}
