package com.ams.restapi.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@Profile("prod")
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOidcUserService customOidcUserService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AdminEmailService adminEmailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(new CustomDeviceAuthenticationFilter(deviceService), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
                auth.requestMatchers("/sections").hasAnyAuthority("ROLE_ADMIN", "ROLE_INSTRUCTOR");
                auth.requestMatchers(HttpMethod.POST, "/attendance").permitAll();
                auth.requestMatchers(HttpMethod.GET, "/attendance").permitAll(); // TODO: temporary to allow for testing, REMOVE LATER
                auth.requestMatchers(HttpMethod.PUT, "/timeConfig/**").permitAll(); // TODO: temporary to allow for testing, REMOVE LATER
                auth.requestMatchers(HttpMethod.GET, "/timeConfig/**").permitAll(); // TODO: temporary to allow for testing, REMOVE LATER
                auth.requestMatchers(HttpMethod.PUT, "/courseInfo/**").permitAll(); // TODO: temporary to allow for testing, REMOVE LATER
                auth.requestMatchers(HttpMethod.GET, "/courseInfo/**").permitAll(); // TODO: temporary to allow for testing, REMOVE LATER
                auth.requestMatchers(HttpMethod.POST, "/readers").permitAll();
                auth.requestMatchers(HttpMethod.GET, "/readers").hasAnyAuthority("ROLE_ADMIN", "ROLE_INSTRUCTOR");
                auth.requestMatchers(HttpMethod.PUT, "/readers").hasAnyAuthority("ROLE_ADMIN", "ROLE_INSTRUCTOR");
                auth.requestMatchers("/esp/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_INSTRUCTOR");
                auth.anyRequest().authenticated();
            })
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())  
                .ignoringRequestMatchers("/readers/**")
                .ignoringRequestMatchers("/timeConfig/**")
                .ignoringRequestMatchers("/courseInfo/**")
            )
            .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(customOidcUserService)
                )
            )
            .formLogin(withDefaults()) // can be removed to jump straight to Google OAuth
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter(adminEmailService));
        return jwtConverter;
    }
}

final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
	private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
		/*
		 * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
		 * the CsrfToken when it is rendered in the response body.
		 */
		this.delegate.handle(request, response, csrfToken);
	}

	@Override
	public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
		/*
		 * If the request contains a request header, use CsrfTokenRequestAttributeHandler
		 * to resolve the CsrfToken. This applies when a single-page application includes
		 * the header value automatically, which was obtained via a cookie containing the
		 * raw CsrfToken.
		 */
		if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
			return super.resolveCsrfTokenValue(request, csrfToken);
		}
		/*
		 * In all other cases (e.g. if the request contains a request parameter), use
		 * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
		 * when a server-side rendered form includes the _csrf request parameter as a
		 * hidden input.
		 */
		return this.delegate.resolveCsrfTokenValue(request, csrfToken);
	}
}

final class CsrfCookieFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
		// Render the token value to a cookie by causing the deferred token to be loaded
		csrfToken.getToken();

		filterChain.doFilter(request, response);
	}
}