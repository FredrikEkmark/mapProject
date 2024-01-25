package com.fredrik.mapProject.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Roles {

    ADMIN("GET_POST"),
    USER("GET");

    private final String permissions;

    Roles(String permissions) {
        this.permissions = permissions;
    }

    public String getPermissions() {
        return permissions;
    }

    public List<GrantedAuthority> splitPermissions() {
        String[] permissionsArray = permissions.split("_");

        return Arrays.stream(permissionsArray)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public List<GrantedAuthority> getAuthorities() {

        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + name());
        List<GrantedAuthority> permissions = new ArrayList<>();

        permissions.add(role);
        permissions.addAll(splitPermissions()); // [GET, POST]

        return permissions;     // [ROLE_ADMIN, GET, POST]
    }

}
