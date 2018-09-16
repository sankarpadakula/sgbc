package com.sp.sgbc.configuration;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomJsonDateSerializer extends JsonDeserializer<Date> {

  private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

  @Override
  public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    String str = jsonParser.getText().trim();
    try {
      return dateFormat.parse(str);
    } catch (ParseException e) {

    }
    return deserializationContext.parseDate(str);
  }
}
