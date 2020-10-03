package me.jordy.rest.sample.accounts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.core.serializer.Serializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 이 Serializer는 특정 도메인의 FK로 포함된 Account의 정보 양을 제한하는데 사용 됨.
 */
public class AccountSerializer extends JsonSerializer<Account> {

    @Override
    public void serialize(Account account, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", account.getId());
        gen.writeEndObject();
    }
}
