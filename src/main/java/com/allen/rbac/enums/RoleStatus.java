package com.allen.rbac.enums;

public enum RoleStatus {

    DISABLE(0, "禁用"), ENABLE(1, "启用");

    private int id;

    private String name;

    private RoleStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static RoleStatus get(int id) {
        for(RoleStatus roleStatus : RoleStatus.values()) {
            if(roleStatus.getId() == id) {
                return roleStatus;
            }
        }
        return null;
    }

}
