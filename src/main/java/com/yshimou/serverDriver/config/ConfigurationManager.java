package com.yshimou.serverDriver.config;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.FileReader;

import static com.yshimou.serverDriver.util.ConfigurationManagerUtil.appendToStringBuffer;
import static com.yshimou.serverDriver.util.ConfigurationManagerUtil.getFileFromPath;
import static com.yshimou.serverDriver.util.ConfigurationManagerUtil.mapJsonToConfiguration;
import static com.yshimou.serverDriver.util.ConfigurationManagerUtil.parseIntoJsonNode;

public class ConfigurationManager {

  private static ConfigurationManager configurationManager;

  private static Configuration currentConfiguration;

  private ConfigurationManager() {
  }

  public static ConfigurationManager getInstance() {
    if (configurationManager == null) {
      configurationManager = new ConfigurationManager();
    }
    return configurationManager;
  }


  /**
   * Load configuration file based on the path provided
   *
   * @param path
   *          path to configuration file
   */

  public void loadConfigurationFile(String path){

    //Get the file from the path
    FileReader fileReader = getFileFromPath(path);

    //Read the file content line by line
    StringBuffer stringBuffer = new StringBuffer();
    appendToStringBuffer(fileReader, stringBuffer);

    //use the resulted string to parse it into a JsonNode
    JsonNode confJson = parseIntoJsonNode(stringBuffer);

    //map the JsonNode to Configuration object
    currentConfiguration = mapJsonToConfiguration(confJson);
  }


  /**
   * Get current loaded configuration
   */
  public Configuration getCurrentConfiguration() {
    if(currentConfiguration == null) {
      throw new HttpConfigurationException("Configuration not loaded");
    }
    return currentConfiguration;
  }
}
