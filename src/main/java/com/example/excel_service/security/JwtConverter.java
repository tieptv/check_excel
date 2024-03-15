package com.example.excel_service.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtConverterProperties properties;

    public JwtConverter(JwtConverterProperties properties) {
        this.properties = properties;
    }

    public static final String PREFIX_REALM_ROLE = "ROLE_realm_";

    public static final String PREFIX_RESOURCE_ROLE = "ROLE_";

    private static final String CLAIM_REALM_ACCESS = "realm_access";

    private static final String CLAIM_RESOURCE_ACCESS = "resource_access";

    private static final String CLAIM_ROLES = "roles";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (properties.getPrincipalAttribute() != null) {
            claimName = properties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Map<String, Collection<String>> realmAccess = jwt.getClaim(CLAIM_REALM_ACCESS);


        if (realmAccess != null && !realmAccess.isEmpty()) {
            Collection<String> roles = realmAccess.get(CLAIM_ROLES);
            if (roles != null && !roles.isEmpty()) {
                Collection<GrantedAuthority> realmRoles = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(PREFIX_REALM_ROLE + role))
                        .collect(Collectors.toList());
                grantedAuthorities.addAll(realmRoles);
            }
        }


        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim(CLAIM_RESOURCE_ACCESS);
        if (resourceAccess != null && !resourceAccess.isEmpty()) {
            resourceAccess.forEach((resource, resourceClaims) -> {
                resourceClaims.get(CLAIM_ROLES).forEach(
                        role -> grantedAuthorities.add(new SimpleGrantedAuthority(PREFIX_RESOURCE_ROLE + resource + "_" + role))
                );
            });
        }

        return grantedAuthorities;
    }
}
