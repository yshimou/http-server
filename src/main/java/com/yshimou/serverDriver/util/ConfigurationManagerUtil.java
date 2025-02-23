package com.yshimou.serverDriver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.yshimou.serverDriver.config.Configuration;
import com.yshimou.serverDriver.config.HttpConfigurationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManagerUtil {


    public static FileReader getFileFromPath(String path) {
    try {
      return new FileReader(path);
    } catch (FileNotFoundException e) {
      throw new HttpConfigurationException("Configuration file not found", e);
    }
  }

  public static void appendToStringBuffer(FileReader fileReader, StringBuffer stringBuffer) {
    try {
      int fileLine;
      for(fileLine = fileReader.read(); fileLine != -1 ; fileLine=fileReader.read()){
        stringBuffer.append((char) fileLine);
      }
    } catch (IOException e) {
      throw new HttpConfigurationException("Error while reading File", e);
    }
  }

  public static JsonNode parseIntoJsonNode(StringBuffer stringBuffer) {
    try {
      return Json.parse(stringBuffer.toString());
    } catch (IOException e) {
      throw new HttpConfigurationException("Error parsing configuration file", e);
    }
  }

  public static Configuration mapJsonToConfiguration(JsonNode confJson) {
    try {
      return Json.fromJson(confJson, Configuration.class);
    } catch (JsonProcessingException e) {
      throw new HttpConfigurationException("Error mapping configuration file to Configuration object", e);
    }
  }
}
