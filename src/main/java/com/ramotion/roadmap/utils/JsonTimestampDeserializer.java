package com.ramotion.roadmap.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ramotion.roadmap.config.AppConfig;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;

public class JsonTimestampDeserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws IOException {

        Timestamp parsed;
        try {
            parsed = Timestamp.from(AppConfig.DATETIME_FORMATTER.parse(jsonParser.getText()).toInstant());
        } catch (ParseException e) {
            parsed = null;
        }
        return parsed;
    }
}
