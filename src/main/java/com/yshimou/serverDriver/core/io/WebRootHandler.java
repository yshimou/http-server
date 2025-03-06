package com.yshimou.serverDriver.core.io;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

public class WebRootHandler {
  private File webRoot;

  public WebRootHandler(String webRootPath) throws WebRootNotFoundException {
    webRoot = new File(webRootPath);
    if(!webRoot.exists() || !webRoot.isDirectory()) {
      throw new WebRootNotFoundException("Path Provided does not exist or is not a directory");
    }
  }

  private boolean checkIfEndsWithSlash(String relativePath){
    return relativePath.endsWith("/");
  }

  /**
   * This method check if the path provided exists
   * @param relativePath
   * @return true if the path exists, false if not
   */
  private boolean checkIfProvidedRelativePathExists(String relativePath) {
    File file = new File(webRoot, relativePath);

    if(!file.exists()){
      return false;
    }
    try {
      if(file.getCanonicalPath().startsWith(webRoot.getCanonicalPath())){
        return true;
      }
      return false;
    } catch (IOException e) {
      return false;
    }
  }

  public String getFileMimeType(String relativePath) throws FileNotFoundException {
    if(checkIfEndsWithSlash(relativePath)){
      relativePath += "index.html"; //By default serve the index
    }

    if(!checkIfProvidedRelativePathExists(relativePath)){
      throw new FileNotFoundException("File not found : " + relativePath);
    }

    File file = new File(webRoot, relativePath);

    String mimeType = URLConnection.getFileNameMap().getContentTypeFor(file.getName());

    if(StringUtils.isEmpty(mimeType)){
      return "application/octet-stream";
    }

    return mimeType;
  }

  public byte[] getFileByteArrayData(String relativePath) throws FileNotFoundException, ReadFileException {
    if(checkIfEndsWithSlash(relativePath)){
      relativePath += "index.html"; //By default serve the index
    }

    if(!checkIfProvidedRelativePathExists(relativePath)){
      throw new FileNotFoundException("File not found : " + relativePath);
    }

    File file = new File(webRoot, relativePath);
    FileInputStream fileInputStream = new FileInputStream(file);
    byte[] fileBytes = new byte[(int)file.length()];
    try {
      fileInputStream.read(fileBytes);
      fileInputStream.close();
    } catch (IOException e) {
      throw new ReadFileException(e);
    }
    return fileBytes;

  }



}
