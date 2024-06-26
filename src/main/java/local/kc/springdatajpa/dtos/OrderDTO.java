package local.kc.springdatajpa.dtos;

import local.kc.springdatajpa.models.OrderStatus;
import local.kc.springdatajpa.models.PaymentMethod;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    public OrderDTO(String consigneeName, String address, String phone, OrderStatus orderStatus, CustomerDTO customer, Set<OrderDetailDTO> orderDetails, PaymentMethod paymentMethod, WardDTO ward, DistrictDTO district, ProvinceDTO province) {
        this.consigneeName = consigneeName;
        this.address = address;
        this.phone = phone;
        this.orderStatus = orderStatus;
        this.customer = customer;
        this.orderDetails = orderDetails;
        this.paymentMethod = paymentMethod;
        this.ward = ward;
        this.district = district;
        this.province = province;
    }
}