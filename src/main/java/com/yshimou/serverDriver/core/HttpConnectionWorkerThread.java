package com.yshimou.serverDriver.core;

import com.yshimou.serverDriver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {

  private final static Logger logger = LoggerFactory.getLogger(HttpServer.class);
  private Socket socket;

  public HttpConnectionWorkerThread(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    InputStream inputStream = null;
    OutputStream outputStream = null;
    try {
      inputStream = socket.getInputStream();
      outputStream = socket.getOutputStream();

      String html = "<html><body><h1>Hello World</h1></body></html>";

      final String CRLF = "\n\r";

      String response = "HTTP/1.1 200 OK" + CRLF + //Status Line : HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
              "Content-Length: " + html.getBytes().length + CRLF + //Header
              CRLF + //CRLF indicates end of headers
              html + CRLF + CRLF; //Body

      outputStream.write(response.getBytes());

      closeConnection(socket, inputStream, outputStream);


      logger.info("Connection Processing finished...");
    } catch (IOException e) {
      logger.info("Error while with connection");

    } finally {
      try {
        closeConnection(socket,inputStream,outputStream);
      } catch (IOException e) {
      }
    }
  }

  private static void closeConnection(Socket socket, InputStream inputStream,
      OutputStream outputStream)
      throws IOException {
    if (inputStream != null && outputStream != null && socket != null) {
      inputStream.close();
      outputStream.close();
      socket.close();
    }

  }
}
