package com.yshimou.serverDriver;

import com.yshimou.serverDriver.config.Configuration;
import com.yshimou.serverDriver.config.ConfigurationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
  public static void main(String[] args) {

    System.out.println("Server Starting ....");

    ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/config.json");
    Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

    System.out.println("Server started on port: " + configuration.getPort());
    System.out.println("Webroot is: " + configuration.getWebroot());

    try {
      ServerSocket serverSocket = new ServerSocket(configuration.getPort());
      Socket socket = serverSocket.accept();

      InputStream inputStream = socket.getInputStream();
      OutputStream outputStream = socket.getOutputStream();

      String html = "<html><body><h1>Hello World</h1></body></html>";

      final String CRLF = "\n\r";

      String response =
          "HTTP/1.1 200 OK" + CRLF + //Status Line : HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
          "Content-Length: " + html.getBytes().length + CRLF + //Header
          CRLF + //CRLF indicates end of headers
          html + CRLF + CRLF; //Body

      outputStream.write(response.getBytes());

      closeConnection(serverSocket, socket, inputStream, outputStream);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void closeConnection(ServerSocket serverSocket, Socket socket, InputStream inputStream,
      OutputStream outputStream)
      throws IOException {
    inputStream.close();
    outputStream.close();
    socket.close();
    serverSocket.close();
  }
}