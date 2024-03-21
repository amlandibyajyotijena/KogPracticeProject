package com.example.demo.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//5

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityFilterChainConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JWTAuthFilter jwtAuthFilter;

    public SecurityFilterChainConfig(AuthenticationEntryPoint authenticationEntryPoint, JWTAuthFilter jwtAuthFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CORS and CSRF
                .csrf(csrf -> csrf.disable())
                // Configure exception handling
                .exceptionHandling(handling -> {
					try {
						handling.authenticationEntryPoint(authenticationEntryPoint).and()
						// Set session creation policy to STATELESS
						.sessionManagement(management -> {
							try {
								management.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
								// Configure HTTP security
								.authorizeHttpRequests(requests -> requests
								        // Permit all requests
								        .requestMatchers("/api/auth/signup/**","/api/auth/login/**").permitAll()
								        // Endpoints that require authentication can be configured here
								        // .antMatchers("/api/**").authenticated()
								        .anyRequest().authenticated());
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

        // Add JWT authentication filter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
