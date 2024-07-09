package local.kc.springdatajpa.utils.payment;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private int orderId;
    private String idAddress;
}
