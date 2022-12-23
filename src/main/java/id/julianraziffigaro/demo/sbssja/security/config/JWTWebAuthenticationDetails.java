package id.julianraziffigaro.demo.sbssja.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class JWTWebAuthenticationDetails extends WebAuthenticationDetails {

  private static final long serialVersionUID = -1961230577711811621L;

  private final String authHeader = "Authorization";

  private String requestToken;

  public JWTWebAuthenticationDetails(HttpServletRequest request) {
    super(request);
    init(request);
  }

  private void init(HttpServletRequest request) {
    Map<String, String> requestHeadersInMap = getRequestHeadersInMap(request);
    Set<Map.Entry<String, String>> entries = requestHeadersInMap.entrySet();
    log.info("===================================");
    log.info("headers start . . .");
    for (Map.Entry entry : entries) {
      if (entry.getKey().equals("referer")) {
        log.info("requestHeaders: key={}, value={}", entry.getKey(), entry.getValue());
      }
      if (entry.getKey().equals("origin")) {
        log.info("requestHeaders: key={}, value={}", entry.getKey(), entry.getValue());
      }
      if (entry.getKey().equals("x-forwarded-for")) {
        log.info("requestHeaders: key={}, value={}", entry.getKey(), entry.getValue());
      }
      if (entry.getKey().equals("x-real-ip")) {
        log.info("requestHeaders: key={}, value={}", entry.getKey(), entry.getValue());
      }
    }

    log.info("headers end . . .");

    String headerForAuthentication = request.getHeader(this.authHeader);

    if (headerForAuthentication != null && headerForAuthentication.startsWith("Bearer ")) {
      setRequestToken(headerForAuthentication.substring(7));
    }
  }

  private Map<String, String> getRequestHeadersInMap(HttpServletRequest request) {
    Map<String, String> result = new HashMap<>();

    Enumeration<String> headerNames = request.getHeaderNames();

    while (headerNames.hasMoreElements()) {
      String key = headerNames.nextElement();
      String value = request.getHeader(key);
      result.put(key, value);
    }

    return result;
  }

  public String getRequestToken() {
    return this.requestToken;
  }

  public void setRequestToken(String requestToken) {
    this.requestToken = requestToken;
  }
}
