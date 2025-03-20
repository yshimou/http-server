package com.yshimou.serverDriver.core;

import com.yshimou.http.HttpParser;
import com.yshimou.http.HttpParsingException;
import com.yshimou.http.HttpRequest;
import com.yshimou.serverDriver.HttpServer;
import com.yshimou.serverDriver.core.io.ReadFileException;
import com.yshimou.serverDriver.core.io.WebRootHandler;
import com.yshimou.serverDriver.core.io.WebRootNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

public class HttpConnectionWorkerThread extends Thread {

  private final static Logger logger = LoggerFactory.getLogger(HttpServer.class);
  private Socket socket;
  private String webRoot;
  private HttpParser httpParser;

  public HttpConnectionWorkerThread(Socket socket, String webRoot) {
    this.socket = socket;
    this.webRoot = webRoot;
    this.httpParser = new HttpParser();
  }

  @Override
  public void run() {
    InputStream inputStream = null;
    OutputStream outputStream = null;
    try {
      inputStream = socket.getInputStream();
      outputStream = socket.getOutputStream();
      WebRootHandler webRootHandler = new WebRootHandler(webRoot);
      HttpRequest httpRequest = httpParser.parseHttpRequest(inputStream);

      String mimeType = webRootHandler.getFileMimeType(httpRequest.getRequestTarget());
      byte[] fileContent = webRootHandler.getFileByteArrayData(httpRequest.getRequestTarget());

      final String CRLF = "\n\r";

      String headers = "HTTP/1.1 200 OK" + CRLF + //Status Line : HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
              "Content-Type: " + mimeType + CRLF + //Header
              "Content-Length: " + fileContent.length + CRLF + //Header
              CRLF ; //CRLF indicates end of headers


      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
      bufferedOutputStream.write(headers.getBytes());
      bufferedOutputStream.flush();

      writeBodyUsingBufferedOutputStream(fileContent, bufferedOutputStream);
      //bufferedOutputStream.write(fileContent);
      //bufferedOutputStream.flush();

      closeConnection(socket, inputStream, outputStream);


      logger.info("Connection Processing finished...");
    } catch (IOException e) {
      logger.info("Error while with connection");

    } catch (WebRootNotFoundException e) {
      //TODO: Handle this exception
    } catch (ReadFileException e) {
      //TODO : handle this exception (bcse of getFileByteArrayData)
    } catch (HttpParsingException e) {
      //TODO: handle this exception bcse of parseHttpRequest
    } finally {
      try {
        closeConnection(socket,inputStream,outputStream);
      } catch (IOException e) {
      }
    }
  }

  private void writeBodyUsingBufferedOutputStream(byte[] fileContent, BufferedOutputStream bufferedOutputStream) {
    try {
      int bufferSize = 8192; //8Kb
      int offset = 0;

      while(offset < fileContent.length){
        int bytesToWrite = Math.min(bufferSize,fileContent.length - offset);
        bufferedOutputStream.write(fileContent,offset,bytesToWrite);
        offset += bytesToWrite;
        bufferedOutputStream.flush();
      }

    } catch (IOException e) {
      e.printStackTrace();
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
