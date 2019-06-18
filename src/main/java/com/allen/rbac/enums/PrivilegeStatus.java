package com.allen.rbac.enums;

import org.springframework.util.StringUtils;

public enum PrivilegeStatus {

    DISABLE(0, "禁用"), ENABLE(1, "启用");

    private int id;

    private String name;

    private PrivilegeStatus(int id, String name) {
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

    public static PrivilegeStatus get(int id) {
        for(PrivilegeStatus privilegeStatus : PrivilegeStatus.values()) {
            if(privilegeStatus.getId() == id) {
                return privilegeStatus;
            }
        }
        return null;
    }

}
