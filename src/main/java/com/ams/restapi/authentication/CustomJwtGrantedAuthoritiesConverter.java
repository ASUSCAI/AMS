package com.ams.restapi.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    
    private final AdminEmailService adminEmailService;

    public CustomJwtGrantedAuthoritiesConverter(AdminEmailService adminEmailService) {
        this.adminEmailService = adminEmailService;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        if (adminEmailService.isAdminEmail(email)) {
            Enrollment adminEnrollment = new Enrollment();
            adminEnrollment.setRole(Enrollment.RoleType.ADMIN);
            authorities.add(new SimpleGrantedAuthority(adminEnrollment.getAuthority()));
        }

        return authorities;
    }
}


