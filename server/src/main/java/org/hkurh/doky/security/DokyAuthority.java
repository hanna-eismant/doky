package org.hkurh.doky.security;

import org.springframework.security.core.GrantedAuthority;

public enum DokyAuthority implements GrantedAuthority {
    ROLE_USER(Role.ROLE_USER);

    private final String authority;

    DokyAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static class Role {
        public static final String ROLE_USER = "ROLE_USER";
    }
}
