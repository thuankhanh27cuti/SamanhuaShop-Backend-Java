package local.kc.springdatajpa.models;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    MONEY(1), BANK(2);
    private final int value;

    PaymentMethod(int value) {
        this.value = value;
    }

}
