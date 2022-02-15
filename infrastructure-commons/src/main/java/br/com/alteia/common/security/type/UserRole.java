package br.com.alteia.common.security.type;

import org.springframework.security.core.GrantedAuthority;

public class UserRole implements GrantedAuthority {
    public static final String INTERNAL_USER_ROLE = "internal_user";

    private final String authority;

    public UserRole(String role) {
        this.authority = role;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
