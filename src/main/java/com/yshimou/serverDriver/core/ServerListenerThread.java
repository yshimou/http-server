package com.yshimou.serverDriver.core;

import com.yshimou.serverDriver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {

  private final static Logger logger = LoggerFactory.getLogger(HttpServer.class);

  private int port;
  private String webroot;
  private ServerSocket serverSocket;

  public ServerListenerThread(int port, String webroot) throws IOException {
    this.port = port;
    this.webroot = webroot;
    this.serverSocket = new ServerSocket(this.port);
  }

  @Override
  public void run() {
    try {
      //while server is healthy and open
      while (serverSocket.isBound() && !serverSocket.isClosed()) {

        Socket socket = serverSocket.accept();
        logger.info("Connection accepted from " + socket.getInetAddress());

        HttpConnectionWorkerThread httpConnectionWorkerThread = new HttpConnectionWorkerThread(socket);
        httpConnectionWorkerThread.start();
      }

    } catch (IOException e) {
      logger.info("Error while with socket setting");
    } finally {
      if (serverSocket != null) {
        try {
          serverSocket.close();
        } catch (IOException e) {}
      }
    }
  }

}
