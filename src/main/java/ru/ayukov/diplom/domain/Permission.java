package ru.ayukov.diplom.domain;

public enum Permission {
    DEVELOPERS_PER("developer"),
    SENIOR_DEVELOPER_PER("seniorDeveloper"),
    MASTER_PER("master"),
    OWNER_PER("owner"),
    ADMIN_PER("admin");

    private final String permission;

    Permission(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
