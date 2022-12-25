package id.julianraziffigaro.demo.sbssja.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final JWTAuthenticationManager jwtAuthenticationManager;

  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .exceptionHandling()
      .authenticationEntryPoint((request, response, authException) -> {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\" }");
      });

    http
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .csrf().disable()
      .httpBasic().disable()
      .formLogin().disable()
      .logout().disable()
      .addFilterAt(new JWTSecurityFilter(jwtAuthenticationManager, restAuthenticationEntryPoint), AnonymousAuthenticationFilter.class)
      .authorizeRequests()
      .antMatchers(HttpMethod.POST, "/login", "/register").permitAll()
      .anyRequest()
      .fullyAuthenticated();

    return http.build();
  }
}
