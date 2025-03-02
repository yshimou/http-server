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


  public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException {
    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

    HttpRequest request = new HttpRequest();
    parseRequestLine(reader, request);
    parseHeaders(reader, request);
    parseBody(reader,request);

    return request;
  }

  private void parseBody(InputStreamReader reader, HttpRequest request) {
  }

  private void parseHeaders(InputStreamReader reader, HttpRequest request) {
    
  }

  private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException {
    int _byte;
    while ((_byte = reader.read()) >= 0) {
      if(_byte == CR) {
        _byte = reader.read();
        if(_byte == LF) {
          return ;
        }
      }
    }
  }
}
