package com.ramotion.roadmap.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ramotion.roadmap.config.AppConfig;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class JsonTimestampSerializer extends JsonSerializer<Timestamp> {

    @Override
    public void serialize(Timestamp date,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        String formattedDate = AppConfig.DATETIME_FORMATTER.format(new Date(date.getTime()));
        jsonGenerator.writeString(formattedDate);
    }
}
