package local.kc.springdatajpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import local.kc.springdatajpa.models.Role;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Role convertToEntityAttribute(Integer dbData) {
        return Stream.of(Role.values())
                .filter(role -> role.getValue() == dbData).findFirst()
                .orElse(null);
    }
}
