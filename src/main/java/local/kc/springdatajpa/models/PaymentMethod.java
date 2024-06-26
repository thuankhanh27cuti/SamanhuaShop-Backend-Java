package local.kc.springdatajpa.models;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    MONEY(0), BANK(1);
    private final int value;

    PaymentMethod(int value) {
        this.value = value;
    }

}
