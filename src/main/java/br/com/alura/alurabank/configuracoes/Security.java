package br.com.alura.alurabank.configuracoes;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class Security {


    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public Security() {
        this.jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new MyJwtConverter());
        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/me").hasRole("customer")
                .antMatchers("/me").hasAuthority("profile")
                .anyRequest().authenticated()
                .and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter);

        return http.build();
    }

    static class MyJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        private static final String ROLE_PREFIX = "ROLE_";
        private static final String ROLES_CLAIM = "realm_access";
        private static final String INNER_ROLES_CLAIM = "roles";
        private final JwtGrantedAuthoritiesConverter scopeConverter;

        MyJwtConverter() {
            scopeConverter = new JwtGrantedAuthoritiesConverter();
            scopeConverter.setAuthorityPrefix("");
        }

        @Override
        public Collection<GrantedAuthority> convert(Jwt source) {
            Collection<GrantedAuthority> grantedAuthorities = scopeConverter.convert(source);

            if(source.hasClaim(ROLES_CLAIM)) {
                var roles = extractRolesFrom(source);
                grantedAuthorities.addAll(roles);
            }

            return grantedAuthorities;
        }

        private List<GrantedAuthority> extractRolesFrom(Jwt source) {
            var realmAccess = ((JSONObject) source.getClaim(ROLES_CLAIM));

            if (!realmAccess.containsKey(INNER_ROLES_CLAIM)) {
                return List.of();
            }

            var roles = (JSONArray) realmAccess.get(INNER_ROLES_CLAIM);

            return roles
                    .stream()
                    .map(name -> new SimpleGrantedAuthority(ROLE_PREFIX + name))
                    .collect(Collectors.toList());
        }
    }

}
