package com.ramotion.roadmap.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ramotion.roadmap.config.AppConfig;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

public class JsonTimestampDeserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        try {
            Date parsed = AppConfig.DATETIME_FORMATTER.parse(jsonParser.getText());
            return new Timestamp(parsed.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}
