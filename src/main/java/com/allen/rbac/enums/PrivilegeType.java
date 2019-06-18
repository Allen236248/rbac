package com.allen.rbac.enums;

import org.springframework.util.StringUtils;

public enum PrivilegeType {

    MENU("menu"), OPERATION("operation");

    private String name;

    private PrivilegeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static PrivilegeType get(String name) {
        if(StringUtils.isEmpty(name))
            return null;

        for(PrivilegeType type : PrivilegeType.values()) {
            if(type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

}
