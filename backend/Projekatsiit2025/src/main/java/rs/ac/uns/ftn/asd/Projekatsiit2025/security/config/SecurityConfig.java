package rs.ac.uns.ftn.asd.Projekatsiit2025.security.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import rs.ac.uns.ftn.asd.Projekatsiit2025.security.auth.RestAuthenticationEntryPoint;
import rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

  @Autowired 
  private JwtRequestFilter jwtRequestFilter;
  
  @Autowired
  private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint((AuthenticationEntryPoint) restAuthenticationEntryPoint))
        .authorizeHttpRequests(auth -> auth
    		.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/auth/register").permitAll()
            .requestMatchers("/api/auth/redirect").permitAll()
            .requestMatchers("/api/auth/activate/**").permitAll()
            .requestMatchers("/api/vehicles").permitAll()
            .requestMatchers("/api/rides/estimate").permitAll()
            .requestMatchers("/sendMail").permitAll()
            .requestMatchers("/sendMailWithAttachment").permitAll()
            .requestMatchers("/api/auth/activate").permitAll()
            .requestMatchers("/favicon.ico").permitAll()
            .requestMatchers("/ws/**").permitAll()
            .requestMatchers("/api/auth/forgot-password").permitAll()
            .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()
            .requestMatchers("/api/auth/reset-password").permitAll()
            
            .anyRequest().authenticated()
      )
      .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration conf) throws Exception {
    return conf.getAuthenticationManager(); // :contentReference[oaicite:22]{index=22}
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // :contentReference[oaicite:23]{index=23}
  }
  
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
      configuration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "OPTIONS", "DELETE", "PATCH")); // or simply "*"
      configuration.setAllowedHeaders(Arrays.asList("*"));
      configuration.setAllowCredentials(true);
      configuration.setExposedHeaders(Arrays.asList("Authorization"));
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
  }
}