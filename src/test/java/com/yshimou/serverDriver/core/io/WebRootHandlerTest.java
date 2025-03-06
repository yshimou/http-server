package com.yshimou.serverDriver.core.io;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebRootHandlerTest {
  WebRootHandler webRootHandler;
  private Method checkIfEndsWithSlashMethod;
  private Method checkIfProvidedRelativePathExistsMethod;

  @BeforeAll
  public void beforeClass() throws WebRootNotFoundException, NoSuchMethodException {
    webRootHandler = new WebRootHandler("webroot");
    //reflection call a certain private method
    Class<WebRootHandler> cls = WebRootHandler.class;

    checkIfEndsWithSlashMethod = cls.getDeclaredMethod("checkIfEndsWithSlash",String.class);
    checkIfEndsWithSlashMethod.setAccessible(true);

    checkIfProvidedRelativePathExistsMethod = cls.getDeclaredMethod("checkIfProvidedRelativePathExists",String.class);
    checkIfProvidedRelativePathExistsMethod.setAccessible(true);
  }

  @Test
  void constructorGoodPath(){
    try {
      WebRootHandler webRootHandler = new WebRootHandler("D:\\Pers\\learning-projects\\http-server\\webroot");
    } catch (WebRootNotFoundException e) {
      fail(e);
    }
  }

  @Test
  void constructorBadPath(){
    try {
      WebRootHandler webRootHandler = new WebRootHandler("");
      fail();
    } catch (WebRootNotFoundException e) {
    }
  }

  @Test
  void constructorRelativePath(){
    try {
      WebRootHandler webRootHandler = new WebRootHandler("webroot");
    } catch (WebRootNotFoundException e) {
      fail(e);
    }
  }

  @Test
  void checkIfEndsWithSlashError(){
    try {
      boolean result = (Boolean)checkIfEndsWithSlashMethod.invoke(webRootHandler,"index.html");
      assertFalse(result);
    } catch (IllegalAccessException e) {
      fail(e);
    } catch (InvocationTargetException e) {
      fail(e);
    }
  }

  @Test
  void checkIfEndsWithSlashMethodGood(){
    try {
      boolean result = (Boolean)checkIfEndsWithSlashMethod.invoke(webRootHandler,"webroot/");
      assertTrue(result);
    } catch (IllegalAccessException e) {
      fail(e);
    } catch (InvocationTargetException e) {
      fail(e);
    }
  }

  @Test
  void testWebRootFilePathExists() {
    try {
      boolean result = (boolean)checkIfProvidedRelativePathExistsMethod.invoke(webRootHandler, "/index.html");
      assertTrue(result);
    } catch (IllegalAccessException e) {
      fail(e);
    } catch (InvocationTargetException e) {
      fail(e);
    }
  }

    @Test
  void testWebRootFilePathNotExists() {
    try {
      boolean result = (boolean)checkIfProvidedRelativePathExistsMethod.invoke(webRootHandler, "/indexxx.html");
      assertFalse(result);
    } catch (IllegalAccessException e) {
      fail(e);
    } catch (InvocationTargetException e) {
      fail(e);
    }
  }

  @Test
  void testGetFileMimeTypeText() {
    try {
      String mimeType = webRootHandler.getFileMimeType("/");
      assertEquals("text/html",mimeType);
    } catch (FileNotFoundException e) {
      fail(e);
    }
  }

  @Test
  void testGetFileMimeTypeGif() {
    try {
      String mimeType = webRootHandler.getFileMimeType("/willem.gif");
      assertEquals("image/gif",mimeType);
    } catch (FileNotFoundException e) {
      fail(e);
    }
  }

  @Test
  void testGetFileByteArrayData() {
    try {
      assertTrue(webRootHandler.getFileByteArrayData("/").length >0);
    } catch (FileNotFoundException e) {
      fail(e);
    } catch (ReadFileException e) {
      fail(e);
    }
  }

  @Test
  void testGetFileByteArrayDataNotTHere() {
    try {
      assertTrue(webRootHandler.getFileByteArrayData("/qwe.html").length >0);
      fail();
    } catch (FileNotFoundException e) {
      //pass
    } catch (ReadFileException e) {
      fail(e);
    }
  }
}
