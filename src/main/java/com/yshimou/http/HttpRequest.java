package com.yshimou.http;

public class HttpRequest extends HttpMessage{

  private String method ;

  private String requestTarget;

  private String httpVersion;

  public String getMethod() {
    return method;
  }

  protected void setMethod(String method) {
    this.method = method;
  }

  protected HttpRequest() {

  }
}
