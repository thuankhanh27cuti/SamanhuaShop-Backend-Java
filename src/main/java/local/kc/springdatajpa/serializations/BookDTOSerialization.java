package local.kc.springdatajpa.serializations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import local.kc.springdatajpa.dtos.BookDTO;
import local.kc.springdatajpa.dtos.CategoryDTO;
import local.kc.springdatajpa.dtos.ImageDTO;
import local.kc.springdatajpa.dtos.OptionDTO;

import java.io.IOException;
import java.util.Set;

public class BookDTOSerialization extends JsonSerializer<BookDTO> {
    @Override
    public void serialize(BookDTO bookDTO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if (bookDTO.getId() != null) {
            jsonGenerator.writeNumberField("id", bookDTO.getId());
        }
        jsonGenerator.writeStringField("name", bookDTO.getName());
        jsonGenerator.writeStringField("image", bookDTO.getImage());
        jsonGenerator.writeNumberField("price", bookDTO.getPrice());
        jsonGenerator.writeStringField("description", bookDTO.getDescription());
        jsonGenerator.writeStringField("createAt", bookDTO.getCreateAt() != null ? bookDTO.getCreateAt().toString(): null);

        Set<CategoryDTO> categories = bookDTO.getCategories();
        if (categories != null) {
            jsonGenerator.writeArrayFieldStart("categories");
            categories.forEach(categoryDTO -> {
                try {
                    jsonGenerator.writeStartObject();
                    if (categoryDTO.getId() != null) {
                        jsonGenerator.writeNumberField("id", categoryDTO.getId());
                    }
                    jsonGenerator.writeStringField("name", categoryDTO.getName());
                    jsonGenerator.writeStringField("image", categoryDTO.getImage());
                    jsonGenerator.writeEndObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            jsonGenerator.writeEndArray();
        }

        Set<OptionDTO> options = bookDTO.getOptions();
        if (options != null) {
            jsonGenerator.writeArrayFieldStart("options");
            options.forEach(optionDTO -> {
                try {
                    jsonGenerator.writeStartObject();
                    if (optionDTO.getId() != null) {
                        jsonGenerator.writeNumberField("id", optionDTO.getId());
                    }
                    jsonGenerator.writeStringField("name", optionDTO.getName());
                    jsonGenerator.writeStringField("image", optionDTO.getImage());
                    jsonGenerator.writeNumberField("quantity", optionDTO.getQuantity());
                    jsonGenerator.writeEndObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            jsonGenerator.writeEndArray();
        }

        Set<ImageDTO> images = bookDTO.getImages();
        if (images != null) {
            jsonGenerator.writeArrayFieldStart("images");
            images.forEach(imageDTO -> {
                try {
                    jsonGenerator.writeStartObject();
                    if (imageDTO.getId() != null) {
                        jsonGenerator.writeNumberField("id", imageDTO.getId());
                    }
                    jsonGenerator.writeStringField("src", imageDTO.getSrc());
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
