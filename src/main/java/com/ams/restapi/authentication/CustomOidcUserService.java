package com.ams.restapi.authentication;

import com.ams.restapi.RootController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomOidcUserService extends OidcUserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOidcUserService.class);

    @Autowired
    private RootController controller;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        logger.info("Attempting to find or create user with email: {}", email);

        String name = oidcUser.getAttribute("name");
        HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(currentRequest, null);
        User user = null;

        if (savedRequest != null) {
            String courseId = null;
            String[] courseIds = savedRequest.getParameterValues("courseId");
            if (courseIds != null && courseIds.length > 0) {
                String courseIdRaw = courseIds[0];
                // Extract only the numeric part of the courseId, before any slashes
                Matcher matcher = Pattern.compile("^\\d+").matcher(courseIdRaw);
                if (matcher.find()) {
                    courseId = matcher.group(); // This extracts '85' even from '85/sections'
                    logger.info("Parsed courseId: {}", courseId);
                    user = userService.findOrCreateUser(email, name, courseId);
                } else {
                    logger.warn("Parsed courseId is invalid or not present in the request.");
                }
            }
        } else {
            logger.warn("No saved request found in the session.");
        }

        if (user != null) {
            Set<GrantedAuthority> combinedAuthorities = new HashSet<>(oidcUser.getAuthorities());
            user.getEnrollments().forEach(enrollment -> combinedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + enrollment.getRole().name())));
            return new DefaultOidcUser(combinedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        } else {
            // Handle the case where user is null, e.g., by logging, throwing an exception, or defaulting to oidcUser's authorities
            logger.error("User could not be found or created. Defaulting to OIDC user's authorities.");
            return oidcUser;
        }
    }

}