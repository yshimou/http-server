package com.yshimou.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class HttpParserTest {

  private static HttpParser httpParser;

  @BeforeAll
  public static void setUp(){
    httpParser = new HttpParser();
  }

  @Test
  void parseHttpRequest() {
    HttpRequest request = null;
    try {
      request = httpParser.parseHttpRequest(generateValidTestCase());
    } catch (HttpParsingException e) {
      fail(e);
    }

    assertNotNull(request);
    assertEquals(request.getMethod(),HttpMethod.GET);
    assertEquals(request.getRequestTarget(), "/");
    assertEquals(request.getBestCompatibleHttpVersion(), HttpVersion.HTTP_1_1);
    assertEquals(request.getOriginalHttpVersion(), "HTTP/1.1");
  }

    @Test
  void parseHttpRequestWithHigherHttpVersion() {
    HttpRequest request = null;
    try {
      request = httpParser.parseHttpRequest(generateHigherHttpVersion());
    } catch (HttpParsingException e) {
      fail(e);
    }

    assertNotNull(request);
    assertEquals(request.getMethod(),HttpMethod.GET);
    assertEquals(request.getRequestTarget(), "/");
    assertEquals(request.getBestCompatibleHttpVersion(), HttpVersion.HTTP_1_1);
    assertEquals(request.getOriginalHttpVersion(), "HTTP/1.4");
  }

  @Test
  void parseHttpRequestBadMethod() {
    try {
      HttpRequest request = httpParser.parseHttpRequest(generateBadMethodNameCase());
      fail();
    } catch (HttpParsingException e) {
      assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }
  }

  @Test
  void parseHttpRequestBadMethodTooLong() {
    try {
      httpParser.parseHttpRequest(generateBadMethodNameTooLongCase());
      fail();
    } catch (HttpParsingException e) {
      assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }
  }

  @Test
  void parseHttpRequestBadCaseInvalidNumberOfItems() {
    try {
      httpParser.parseHttpRequest(generateBadCaseInvalidNumberOfItems());
      fail();
    } catch (HttpParsingException e) {
      assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
    }
  }

  @Test
  void parseHttpRequestBadCaseEmptyRequestLine() {
    try {
      httpParser.parseHttpRequest(generateBadCaseEmptyReqLine());
      fail();
    } catch (HttpParsingException e) {
      assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
    }
  }

    @Test
  void parseHttpRequestBadCaseCRnoLF() {
    try {
      httpParser.parseHttpRequest(generateBadCaseCRnoLF());
      fail();
    } catch (HttpParsingException e) {
      assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
    }
  }

  @Test
  void parseHttpRequestBadHttpVersionTestCase() {
    try {
      httpParser.parseHttpRequest(generateBadHttpVersionTestCase());
      fail();
    } catch (HttpParsingException e) {
      assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
    }
  }


  @Test
  void parseHttpRequestUnsupportedHttpVersion() {
    try {
      httpParser.parseHttpRequest(generateUnsupportedHttpVersion());
      fail();
    } catch (HttpParsingException e) {
      assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
    }
  }


  private InputStream generateValidTestCase() {
    String rawData = "GET / HTTP/1.1\r\n"
        + "Host: localhost:8083\r\n"
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
        return inputStream;
  }

   private InputStream generateBadMethodNameCase() {
    String rawData = "Get / HTTP/1.1\r\n"
        + "\r\n";
    InputStream inputStream = new ByteArrayInputStream(
        rawData.getBytes(
            StandardCharsets.US_ASCII
        )
    );
        return inputStream;
  }

   private InputStream generateBadMethodNameTooLongCase() {
    String rawData = "GETTTTTTTTTTTT / HTTP/1.1\r\n"
        + "\r\n";
    InputStream inputStream = new ByteArrayInputStream(
        rawData.getBytes(
            StandardCharsets.US_ASCII
        )
    );
        return inputStream;
  }

   private InputStream generateBadCaseInvalidNumberOfItems() {
    String rawData = "GET / AAA HTTP/1.1\r\n"
        + "\r\n";
    InputStream inputStream = new ByteArrayInputStream(
        rawData.getBytes(
            StandardCharsets.US_ASCII
        )
    );
        return inputStream;
  }

   private InputStream generateBadCaseEmptyReqLine() {
    String rawData = "\r\n"
        + "\r\n";
    InputStream inputStream = new ByteArrayInputStream(
        rawData.getBytes(
            StandardCharsets.US_ASCII
        )
    );
        return inputStream;
  }

     private InputStream generateBadCaseCRnoLF() {
    String rawData = "GET / HTTP/1.1\r" //no LF
        + "\r\n";
    InputStream inputStream = new ByteArrayInputStream(
        rawData.getBytes(
            StandardCharsets.US_ASCII
        )
    );
        return inputStream;
  }


    private InputStream generateBadHttpVersionTestCase() {
    String rawData = "GET / HTP/1.1\r\n"
        + "Host: localhost:8083\r\n"
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
        return inputStream;
  }

  private InputStream generateUnsupportedHttpVersion() {
    String rawData = "GET / HTTP/2.1\r\n"
        + "Host: localhost:8083\r\n"
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
        return inputStream;
  }


  private InputStream generateHigherHttpVersion() {
    String rawData = "GET / HTTP/1.4\r\n"
        + "Host: localhost:8083\r\n"
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
        return inputStream;
  }


}