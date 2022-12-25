package id.julianraziffigaro.demo.sbssja.security.config;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serial;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class JWTWebAuthenticationDetails extends WebAuthenticationDetails {

  @Serial
  private static final long serialVersionUID = -1961230577711811621L;

  private String requestToken;

  public JWTWebAuthenticationDetails(HttpServletRequest request) {
    super(request);
    init(request);
  }

  private void init(HttpServletRequest request) {
    Map<String, String> requestHeadersInMap = getRequestHeadersInMap(request);
    Set<Map.Entry<String, String>> entries = requestHeadersInMap.entrySet();

    log.info("===================================");
    log.info("headers start...");
    entries.forEach(entry -> log.info("requestHeaders: key={}, value={}", entry.getKey(), entry.getValue()));
    log.info("headers end...");
    log.info("===================================");

    String headerForAuthentication = request.getHeader("authorization");

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
