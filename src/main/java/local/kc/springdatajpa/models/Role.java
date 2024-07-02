package local.kc.springdatajpa.models;

import lombok.Getter;

@Getter
public enum Role {
    OWNER(0),
    STAFF(1),
    USER(2);

    private final int value;

    Role(int value) {
        this.value = value;
    }
}
