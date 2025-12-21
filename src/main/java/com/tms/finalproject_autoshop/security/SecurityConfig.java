package com.tms.finalproject_autoshop.security;
import com.tms.finalproject_autoshop.model.Role;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;

    public SecurityConfig(JwtAuthFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        PathPatternRequestMatcher.Builder pathBuilder = PathPatternRequestMatcher.withDefaults();
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(pathBuilder.matcher(HttpMethod.POST, "/security/registration")).permitAll()
                                .requestMatchers(pathBuilder.matcher("/swagger-ui/**")).permitAll() //TODO: нужно указать ADMIN но мы не указали потому что в браузере нельзя просто добавить хедер
                                .requestMatchers(pathBuilder.matcher("/v3/api-docs/**")).permitAll() //TODO: нужно указать ADMIN но мы не указали потому что в браузере нельзя просто добавить хедер
                                .requestMatchers(pathBuilder.matcher(HttpMethod.POST, "/security/jwt")).permitAll()
                                .requestMatchers(pathBuilder.matcher(HttpMethod.GET, "/user")).permitAll()
                                .requestMatchers(pathBuilder.matcher(HttpMethod.GET, "/user/sort/**")).hasRole(Role.ADMIN.name())
                                .requestMatchers(pathBuilder.matcher(HttpMethod.GET, "/user/pagination/**")).hasRole(Role.ADMIN.name())
                                .requestMatchers(pathBuilder.matcher(HttpMethod.GET, "/user/myself")).hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.MODERATOR.name())
                                .requestMatchers(pathBuilder.matcher(HttpMethod.GET, "/user/**")).permitAll()
                                .requestMatchers(pathBuilder.matcher(HttpMethod.GET, "/user/myself")).hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.MODERATOR.name())
                                .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
