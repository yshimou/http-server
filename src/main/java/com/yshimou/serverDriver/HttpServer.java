package com.yshimou.serverDriver;

import com.yshimou.serverDriver.config.Configuration;
import com.yshimou.serverDriver.config.ConfigurationManager;
import com.yshimou.serverDriver.core.ServerListenerThread;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {
  private final static Logger logger = LoggerFactory.getLogger(HttpServer.class);

  public static void main(String[] args) {

    logger.info("Server Starting ....");

    ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/config.json");
    Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

    logger.info("Server started on port: " + configuration.getPort());
    logger.info("Webroot is: " + configuration.getWebroot());

    try {
      ServerListenerThread serverListenerThread = new ServerListenerThread(configuration.getPort(), configuration.getWebroot());
      serverListenerThread.start();
    } catch (IOException e) {
      e.printStackTrace();
      //TODO handle exception later
    }

  }

}