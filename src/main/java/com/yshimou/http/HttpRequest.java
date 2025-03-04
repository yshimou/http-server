package com.yshimou.http;

import org.apache.commons.lang3.StringUtils;

import static com.yshimou.http.HttpVersion.getBestCompatibleVersion;

public class HttpRequest extends HttpMessage{

  private HttpMethod method ;

  private String requestTarget;

  private String originalHttpVersion; //literal from the request

  private HttpVersion bestCompatibleHttpVersion;

  public String getOriginalHttpVersion() {
    return originalHttpVersion;
  }

  public HttpVersion getBestCompatibleHttpVersion() {
    return bestCompatibleHttpVersion;
  }

  public void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
    this.originalHttpVersion = originalHttpVersion;
    this.bestCompatibleHttpVersion = getBestCompatibleVersion(originalHttpVersion);
    if(this.bestCompatibleHttpVersion == null){
      throw new HttpParsingException(
          HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
      );
    }
  }

  public String getRequestTarget() {
    return requestTarget;
  }

  protected void setRequestTarget(String requestTarget) throws HttpParsingException {
    if(StringUtils.isEmpty(requestTarget)){
      throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
    }
    this.requestTarget = requestTarget;
  }

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
