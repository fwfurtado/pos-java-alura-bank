package br.com.alura.alurabank.controller;

import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class StatusController {

    @GetMapping(path = "/status")
    public String status() {
        return "ok!";
    }


    @GetMapping("/me")
    public Map<String, ?> home(@AuthenticationPrincipal Jwt jwt, Principal principal, Authentication authentication) {

//        var principal = authentication.getPrincipal();

        return Map.of("name", principal.getName(), "roles", jwt.getClaims());
    }

}
