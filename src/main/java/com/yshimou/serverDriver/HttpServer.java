package com.yshimou.serverDriver;

import com.yshimou.serverDriver.config.Configuration;
import com.yshimou.serverDriver.config.ConfigurationManager;

public class HttpServer {
  public static void main(String[] args) {

    System.out.println("Server Starting ....");

    ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/config.json");
    Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

    System.out.println("Server started on port: " + configuration.getPort());
    System.out.println("Webroot is: " + configuration.getWebroot());
  }
}