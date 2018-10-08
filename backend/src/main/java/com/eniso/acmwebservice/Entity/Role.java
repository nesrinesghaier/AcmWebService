package com.eniso.acmwebservice.Entity;

import org.springframework.security.core.GrantedAuthority;

import static com.eniso.acmwebservice.Entity.Constants.AUTHORITY_PREFIX;

public enum Role implements GrantedAuthority {
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return AUTHORITY_PREFIX + this.name();
    }
}
