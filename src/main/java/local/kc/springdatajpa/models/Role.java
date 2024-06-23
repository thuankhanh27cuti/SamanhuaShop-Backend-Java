package local.kc.springdatajpa.models;

import lombok.Getter;

@Getter
public enum Role {
    OWNER(1),
    ADMIN(2),
    USER(3);

    private final int value;

    Role(int value) {
        this.value = value;
    }
}
