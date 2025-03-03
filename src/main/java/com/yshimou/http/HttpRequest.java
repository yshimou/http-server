package com.yshimou.http;

public class HttpRequest extends HttpMessage{

  private HttpMethod method ;

  private String requestTarget;

  private String httpVersion;

  public HttpMethod getMethod() {
    return method;
  }

  protected void setMethod(String methodName) throws HttpParsingException {
    for(HttpMethod method : HttpMethod.values()){
      if(methodName.equals(method.toString())){
        this.method = HttpMethod.valueOf(methodName);
        return;
      }
    }
    throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
  }

  protected HttpRequest() {

  }
}
