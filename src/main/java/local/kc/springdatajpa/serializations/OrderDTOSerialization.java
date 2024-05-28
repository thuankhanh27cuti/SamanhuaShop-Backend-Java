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

                        jsonGenerator.writeObjectFieldStart("book");
                        BookDTO book = optionDTO.getBook();
                        if (book != null) {
                            if (book.getId() != null) {
                                jsonGenerator.writeNumberField("id", book.getId());
                            }
                            jsonGenerator.writeStringField("name", book.getName());
                            jsonGenerator.writeStringField("image", book.getImage());
                            jsonGenerator.writeNumberField("price", book.getPrice());
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

        jsonGenerator.writeEndObject();
    }
}
