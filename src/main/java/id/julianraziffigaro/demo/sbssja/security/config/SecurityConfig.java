package id.julianraziffigaro.demo.sbssja.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
      .and()
      .csrf().disable()
      .httpBasic().disable()
      .formLogin().disable()
      .logout().disable()
      .authorizeRequests()
      .antMatchers(HttpMethod.POST, "/login").permitAll()
      .anyRequest()
      .fullyAuthenticated();

    return http.build();
  }
}
