package com.prs.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**","/api/v1/search/**",
	            "/v2/api-docs",
	            "/v3/api-docs",
	            "/v3/api-docs/**",
	            "/oauth2/authorization/**",//Add these two for login
	            "/login/oauth2/code/**",   //
	            "/swagger-resources",
	            "/swagger-resources/**",
	            "/configuration/ui",
	            "/configuration/security",
	            "/swagger-ui/**",
	            "/webjars/**",
	            "/swagger-ui.html"};
	 
	private final JwtAuthenticationFilter jwtAuthFilter;

	private final UserDetailsService userDetailsService;

	//private final LogoutHandler logoutHandler;
	
	// Constructor injection for required dependencies
	public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}

	/*
	 * Main security configuration Defines endpoint access rules and JWT filter
	 * setup
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// Disable CSRF (not needed for stateless JWT)
				.csrf(csrf -> csrf.disable())

				// Configure endpoint authorization
				.authorizeHttpRequests(auth -> auth
						// Public endpoints
						.requestMatchers(WHITE_LIST_URL).permitAll()

						// Role-based endpoints
						.requestMatchers("/api/v1/user/**").hasAuthority("ROLE_USER")
						.requestMatchers("/api/v1/org/**").hasAuthority("ROLE_ORGANIZATION")
						// All other endpoints require authentication
						.anyRequest().authenticated())

				// Stateless session (required for JWT)
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Enable OAuth2 login here:
//			      .oauth2Login(oauth2 -> oauth2
//			          .loginPage("/oauth2/authorization/google")              // Optional: custom entry point
//			          .defaultSuccessUrl("/api/v1/auth/oauth-success", true)  // Where to go on success
//			      )

				// Set custom authentication provider
				.authenticationProvider(authenticationProvider())
				
				// Add JWT filter before Spring Security's default filter
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//				.logout(logout ->
//                   logout.logoutUrl("/api/v1/auth/logout")
//                        .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//				 );
		http.cors(); // Enable CORS if frontend is on different domain
		return http.build();
	}

	/*
	 * Password encoder bean (uses BCrypt hashing) Critical for secure password
	 * storage
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * Authentication provider configuration Links UserDetailsService and
	 * PasswordEncoder
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	/*
	 * Authentication manager bean Required for programmatic authentication (e.g.,
	 * in /generateToken)
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
}