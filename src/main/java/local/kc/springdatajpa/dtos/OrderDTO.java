package local.kc.springdatajpa.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import local.kc.springdatajpa.models.OrderStatus;
import local.kc.springdatajpa.models.PaymentMethod;
import local.kc.springdatajpa.serializations.OrderDTOSerialization;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = OrderDTOSerialization.class)
public class OrderDTO implements Serializable {
    Integer id;
    String consigneeName;
    String address;
    String phone;
    Date createAt;
    Date finishedAt;
    OrderStatus orderStatus;
    CustomerDTO customer;
    Set<OrderDetailDTO> orderDetails;
    PaymentMethod paymentMethod;
    WardDTO ward;
    DistrictDTO district;
    ProvinceDTO province;
    Set<OrderLogDTO> orderLogs;
}