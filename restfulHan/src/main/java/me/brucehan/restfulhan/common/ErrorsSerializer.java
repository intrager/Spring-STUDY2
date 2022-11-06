package me.brucehan.restfulhan.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

// 스프링이 제공할 수 있는 Errors를 변환하는 Serializer
@JsonComponent // 이렇게 등록하면 objectMapper가 Errors라는 객체가 serialization 할 때 ErrorsSerializer를 사용함
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeFieldName("errors");
        gen.writeStartArray(); // errors 안에는 에러가 여러개니까 배열로 담기 위함
        errors.getFieldErrors().forEach(e -> { // 에러마다
           try {
               gen.writeStartObject(); // json object를 만들고

               gen.writeStringField("field", e.getField()); // 어떤 필드에 해당하는 건지
               gen.writeStringField("objectName", e.getObjectName());
               gen.writeStringField("code", e.getCode());
               gen.writeStringField("defaultMessage", e.getDefaultMessage());
               Object rejectedValue = e.getRejectedValue();
               if(rejectedValue != null) { // rejectedValue가 있을 수도 있고, 없을 수도 있으므로
                    gen.writeStringField("rejectedValue", rejectedValue.toString());
               }
               gen.writeEndObject(); // 닫아줌
           } catch (IOException exception) {
               exception.printStackTrace();
           }
        });

        errors.getGlobalErrors().forEach(e -> {
            try {
                gen.writeStartObject();

                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());

                gen.writeEndObject();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
        gen.writeEndArray(); // errors 안에는 에러가 여러개니까 배열로 담기 위함
    }
}
