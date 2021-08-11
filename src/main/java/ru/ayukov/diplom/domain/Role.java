package ru.ayukov.diplom.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;


public enum Role {
    DEVELOPERS(Set.of(Permission.DEVELOPERS_PER)),
    SENIOR_DEVELOPER(Set.of(Permission.DEVELOPERS_PER,Permission.SENIOR_DEVELOPER_PER)),
    MASTER(Set.of(Permission.SENIOR_DEVELOPER_PER,Permission.MASTER_PER)),
    OWNER(Set.of(Permission.OWNER_PER)),
    ADMIN(Set.of(Permission.ADMIN_PER));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions){
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
