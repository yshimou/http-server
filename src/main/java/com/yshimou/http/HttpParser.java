package com.yshimou.http;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

  private final static Logger log = LoggerFactory.getLogger(HttpParser.class);

  private static final int SP = 0x20;

  private static final int CR = 0x0D;

  private static final int LF = 0x0A;


  public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

    HttpRequest request = new HttpRequest();

    try {
      parseRequestLine(reader, request);
    } catch (IOException e) {
      e.printStackTrace();
    }
    parseHeaders(reader, request);
    parseBody(reader,request);

    return request;
  }

  private void parseBody(InputStreamReader reader, HttpRequest request) {
  }

  private void parseHeaders(InputStreamReader reader, HttpRequest request) {
    
  }

  private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException,
      HttpParsingException {
    StringBuilder processingDataBuffer = new StringBuilder();

    boolean methodParsed = false;
    boolean requestTargetParsed = false;

    int _byte;
    while ((_byte = reader.read()) >= 0) {
      if(_byte == CR) {
        _byte = reader.read();
        if(_byte == LF) {
          //reached at least end of 1st line without method nor target => empty request line
          if(!methodParsed || !requestTargetParsed){
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
          }
          log.debug("Request line VERSION to process : {}",processingDataBuffer.toString());
          try {
            request.setHttpVersion(processingDataBuffer.toString());
          } catch (BadHttpVersionException e) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
          }
          return ;
        //CR and no LF
        } else {
          throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
      }

      if(_byte == SP){
        //TODO process previous data
        if(!methodParsed) {
          log.debug("Request line METHOD to process : {}",processingDataBuffer.toString());
          request.setMethod(processingDataBuffer.toString());
          methodParsed = true;
        } else if (!requestTargetParsed) {
          log.debug("Request line REQ TARGET to process : {}",processingDataBuffer.toString());
          request.setRequestTarget(processingDataBuffer.toString());
          requestTargetParsed = true;
        } else {
          throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        processingDataBuffer.delete(0,processingDataBuffer.length());
      } else {
        processingDataBuffer.append((char)_byte);
        if(!methodParsed && processingDataBuffer.length() > HttpMethod.MAX_LENGTH){
          throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
      }
    }
  }
}
