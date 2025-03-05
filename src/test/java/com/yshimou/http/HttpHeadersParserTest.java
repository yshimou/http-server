package com.yshimou.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HttpHeadersParserTest {

  private HttpParser httpParser;
  private Method parseHeadersMethod;

    @BeforeAll
  public void setUp() throws NoSuchMethodException {
    httpParser = new HttpParser();
    Class<HttpParser> cls = HttpParser.class;
    parseHeadersMethod = cls.getDeclaredMethod("parseHeaders", InputStreamReader.class, HttpRequest.class);
    parseHeadersMethod.setAccessible(true);
  }

  @Test
  public void testSimpleSingleHeader() throws InvocationTargetException, IllegalAccessException {
      HttpRequest httpRequest = new HttpRequest();
      parseHeadersMethod.invoke(httpParser,generateSimpleSingleHeader(), httpRequest);
      assertEquals(1,httpRequest.getHeaderNames().size());
      assertEquals("localhost:8083", httpRequest.getHeader("host"));

  }

  @Test
  public void testSimpleMultipleHeaders() throws InvocationTargetException, IllegalAccessException {
      HttpRequest httpRequest = new HttpRequest();
      parseHeadersMethod.invoke(httpParser,generateMultipleHeaders(), httpRequest);
      assertEquals(15,httpRequest.getHeaderNames().size());
      assertEquals("localhost:8083", httpRequest.getHeader("host"));
  }

   @Test
  public void testErrorSpaceBeforeColonHeader() throws IllegalAccessException {
      HttpRequest httpRequest = new HttpRequest();

     try {
       parseHeadersMethod.invoke(httpParser,generateErrorSpaceBeforeColonHeader(), httpRequest);
       fail();
     } catch (InvocationTargetException e) {
       assertEquals(((HttpParsingException)e.getCause()).getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
     }

   }


  private InputStreamReader generateSimpleSingleHeader () {
    String rawData = "Host: localhost:8083\r\n"
//        + "Connection: keep-alive\r\n"
//        + "Cache-Control: max-age=0\r\n"
//        + "sec-ch-ua: \"Not(A:Brand\";v=\"99\", \"Google Chrome\";v=\"133\", \"Chromium\";v=\"133\"\r\n"
//        + "sec-ch-ua-mobile: ?0\r\n"
//        + "sec-ch-ua-platform: \"Windows\"\r\n"
//        + "Upgrade-Insecure-Requests: 1\r\n"
//        + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36\r\n"
//        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n"
//        + "Sec-Fetch-Site: none\r\n"
//        + "Sec-Fetch-Mode: navigate\r\n"
//        + "Sec-Fetch-User: ?1\r\n"
//        + "Sec-Fetch-Dest: document\r\n"
//        + "Accept-Encoding: gzip, deflate, br, zstd\r\n"
//        + "Accept-Language: en-US,en;q=0.9,fr-FR;q=0.8,fr;q=0.7\r\n"
        + "\r\n";
    InputStream inputStream = new ByteArrayInputStream(
        rawData.getBytes(
            StandardCharsets.US_ASCII
        )
    );
        return new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
  }

  private InputStreamReader generateMultipleHeaders () {
    String rawData = "Host: localhost:8083\r\n"
        + "Connection: keep-alive\r\n"
        + "Cache-Control: max-age=0\r\n"
        + "sec-ch-ua: \"Not(A:Brand\";v=\"99\", \"Google Chrome\";v=\"133\", \"Chromium\";v=\"133\"\r\n"
        + "sec-ch-ua-mobile: ?0\r\n"
        + "sec-ch-ua-platform: \"Windows\"\r\n"
        + "Upgrade-Insecure-Requests: 1\r\n"
        + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36\r\n"
        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n"
        + "Sec-Fetch-Site: none\r\n"
        + "Sec-Fetch-Mode: navigate\r\n"
        + "Sec-Fetch-User: ?1\r\n"
        + "Sec-Fetch-Dest: document\r\n"
        + "Accept-Encoding: gzip, deflate, br, zstd\r\n"
        + "Accept-Language: en-US,en;q=0.9,fr-FR;q=0.8,fr;q=0.7\r\n"
        + "\r\n";
    InputStream inputStream = new ByteArrayInputStream(
        rawData.getBytes(
            StandardCharsets.US_ASCII
        )
    );
        return new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
  }

  private InputStreamReader generateErrorSpaceBeforeColonHeader () {
    String rawData = "Host : localhost:8083\r\n"
        + "\r\n";
    InputStream inputStream = new ByteArrayInputStream(
        rawData.getBytes(
            StandardCharsets.US_ASCII
        )
    );
        return new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
  }

}
