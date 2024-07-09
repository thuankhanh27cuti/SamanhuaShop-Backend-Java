package local.kc.springdatajpa.utils.payment;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse implements Serializable {
    private String code;
    private String message;
    private String data;
}
