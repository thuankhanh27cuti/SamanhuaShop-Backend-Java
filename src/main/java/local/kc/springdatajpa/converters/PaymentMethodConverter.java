package local.kc.springdatajpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import local.kc.springdatajpa.models.PaymentMethod;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PaymentMethod attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public PaymentMethod convertToEntityAttribute(Integer dbData) {
        return Stream.of(PaymentMethod.values())
                .filter(paymentMethod -> paymentMethod.getValue() == dbData)
                .findFirst()
                .orElse(null);
    }
}
