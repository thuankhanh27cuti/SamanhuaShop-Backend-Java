package local.kc.springdatajpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import local.kc.springdatajpa.models.OrderStatus;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderStatus attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer dbData) {
        return Stream.of(OrderStatus.values())
                .filter(status -> status.getValue() == dbData)
                .findFirst()
                .orElse(null);
    }
}
