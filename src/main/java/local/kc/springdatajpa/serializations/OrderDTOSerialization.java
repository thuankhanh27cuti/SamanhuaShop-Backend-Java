package local.kc.springdatajpa.serializations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import local.kc.springdatajpa.dtos.*;

import java.io.IOException;
import java.util.Set;

public class OrderDTOSerialization extends JsonSerializer<OrderDTO> {
    @Override
    public void serialize(OrderDTO orderDTO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if (orderDTO.getId() != null) {
            jsonGenerator.writeNumberField("id", orderDTO.getId());
        }
        jsonGenerator.writeStringField("consigneeName", orderDTO.getConsigneeName());
        jsonGenerator.writeStringField("address", orderDTO.getAddress());
        jsonGenerator.writeStringField("phone", orderDTO.getPhone());
        jsonGenerator.writeStringField("createAt", orderDTO.getCreateAt() != null? orderDTO.getCreateAt().toString(): null);
        jsonGenerator.writeStringField("finishedAt", orderDTO.getFinishedAt() != null? orderDTO.getFinishedAt().toString() : null);
        jsonGenerator.writeStringField("orderStatus", orderDTO.getOrderStatus() != null? orderDTO.getOrderStatus().toString(): null);

        CustomerDTO customer = orderDTO.getCustomer();
        if (customer != null) {
            jsonGenerator.writeObjectFieldStart("customer");
            if (customer.getId() != null) {
                jsonGenerator.writeNumberField("id", customer.getId());
            }
            jsonGenerator.writeStringField("username", customer.getUsername());
            jsonGenerator.writeBooleanField("isDeleted", customer.isDeleted());
            jsonGenerator.writeEndObject();
        }

        Set<OrderDetailDTO> orderDetails = orderDTO.getOrderDetails();
        if (orderDetails != null) {
            jsonGenerator.writeArrayFieldStart("orderDetails");
            orderDetails.forEach(orderDetailDTO -> {
                try {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeNumberField("quantity", orderDetailDTO.getQuantity());
                    jsonGenerator.writeNumberField("price", orderDetailDTO.getPrice());

                    jsonGenerator.writeObjectFieldStart("option");
                    OptionDTO optionDTO = orderDetailDTO.getOption();
                    if (optionDTO != null) {
                        if (optionDTO.getId() != null) {
                            jsonGenerator.writeNumberField("id", optionDTO.getId());
                        }
                        jsonGenerator.writeStringField("name", optionDTO.getName());
                        jsonGenerator.writeBooleanField("isDeleted", optionDTO.isDeleted());

                        jsonGenerator.writeObjectFieldStart("book");
                        BookDTO book = optionDTO.getBook();
                        if (book != null) {
                            if (book.getId() != null) {
                                jsonGenerator.writeNumberField("id", book.getId());
                            }
                            jsonGenerator.writeStringField("name", book.getName());
                            jsonGenerator.writeStringField("image", book.getImage());
                            jsonGenerator.writeNumberField("price", book.getPrice());
                            jsonGenerator.writeBooleanField("isDeleted", book.isDeleted());
                        }
                        jsonGenerator.writeEndObject();
                    }
                    jsonGenerator.writeEndObject();

                    jsonGenerator.writeEndObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            jsonGenerator.writeEndArray();

        }

        jsonGenerator.writeStringField("paymentMethod", orderDTO.getPaymentMethod() != null ? orderDTO.getPaymentMethod().toString(): null);

        WardDTO ward = orderDTO.getWard();
        if (ward != null) {
            jsonGenerator.writeObjectFieldStart("ward");
            if (ward.getCode() != null) {
                jsonGenerator.writeNumberField("code", ward.getCode());
            }
            jsonGenerator.writeStringField("name", ward.getName());
            jsonGenerator.writeStringField("fullName", ward.getFullName());
            jsonGenerator.writeEndObject();
        }

        DistrictDTO district = orderDTO.getDistrict();
        if (district != null) {
            jsonGenerator.writeObjectFieldStart("district");
            if (district.getCode() != null) {
                jsonGenerator.writeNumberField("code", district.getCode());
            }
            jsonGenerator.writeStringField("name", district.getName());
            jsonGenerator.writeStringField("fullName", district.getFullName());
            jsonGenerator.writeEndObject();
        }

        ProvinceDTO province = orderDTO.getProvince();
        if (province != null) {
            jsonGenerator.writeObjectFieldStart("province");
            if (province.getCode() != null) {
                jsonGenerator.writeNumberField("code", province.getCode());
            }
            jsonGenerator.writeStringField("name", province.getName());
            jsonGenerator.writeStringField("fullName", province.getFullName());
            jsonGenerator.writeEndObject();
        }

        Set<OrderLogDTO> orderLogs = orderDTO.getOrderLogs();
        if (orderLogs != null) {
            jsonGenerator.writeArrayFieldStart("orderLogs");
            orderLogs.forEach(orderLogDTO -> {
                try {
                    jsonGenerator.writeStartObject();
                    if (orderLogDTO.getId() != null) {
                        jsonGenerator.writeNumberField("id", orderLogDTO.getId());
                    }
                    jsonGenerator.writeStringField("time", orderLogDTO.getTime() != null ? orderLogDTO.getTime().toString() : null);
                    jsonGenerator.writeStringField("description", orderLogDTO.getDescription());
                    jsonGenerator.writeEndObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndObject();
    }
}
