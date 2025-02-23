package com.yshimou.serverDriver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class Json {

  private static ObjectMapper objectMapper = getObjectMapper();

  private static ObjectMapper getObjectMapper() {
    ObjectMapper defaultObjectMapper = new ObjectMapper();
    //configuration that doesn't fail the mapping in case a field is missing
    defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return defaultObjectMapper;
  }

  public static JsonNode parse(String jsonInput) throws IOException {
    return objectMapper.readTree(jsonInput);
  }

  public static <T> T fromJson(JsonNode jsonNode, Class<T> tclass) throws JsonProcessingException {
    return objectMapper.treeToValue(jsonNode, tclass);
  }

  public static JsonNode toJson(Object object) {
    return objectMapper.valueToTree(object);
  }

  private static String generateJsonString(Object object, boolean pretty) throws JsonProcessingException {
    ObjectWriter objectWriter = objectMapper.writer();
    if (pretty) {
      objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
    }
    return objectWriter.writeValueAsString(object);
  }

  public static String nodeToString(JsonNode node) throws JsonProcessingException {
      return generateJsonString(node, false);
  }

  public static String nodeToStringPretty(JsonNode node) throws JsonProcessingException {
      return generateJsonString(node, true);
  }
}
