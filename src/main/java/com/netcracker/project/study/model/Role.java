package com.netcracker.project.study.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    ROLE_ADMIN, ROLE_DRIVER, ROLE_CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
