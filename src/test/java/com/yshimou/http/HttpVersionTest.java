package com.yshimou.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class HttpVersionTest {

  @Test
  void getBestCompatibleVersionExactMatch() {
    HttpVersion version = null;
    try {
      version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
    } catch (BadHttpVersionException e) {
      fail(e);
    }
    assertNotNull(version);
    assertEquals(version,HttpVersion.HTTP_1_1);
  }

    @Test
  void getBestCompatibleVersionBadFormat(){
    HttpVersion version = null;
    try {
      HttpVersion.getBestCompatibleVersion("HttP/1.1");
      fail();
    } catch (BadHttpVersionException e) {
    }

  }

    @Test
  void getBestCompatibleVersionHigherVersion(){
    HttpVersion version = null;
    try {
      version = HttpVersion.getBestCompatibleVersion("HTTP/1.4");
    } catch (BadHttpVersionException e) {
      fail(e);
    }
    assertNotNull(version);
    assertEquals(version,HttpVersion.HTTP_1_1);

  }





















}
