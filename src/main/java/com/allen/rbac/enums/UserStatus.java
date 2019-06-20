package com.allen.rbac.enums;

public enum UserStatus {

    NORMAL(1, "正常"), LOCKED(2, "已锁定"), DELETED(3, "已删除");

    private int id;

    private String name;

    private UserStatus(int id, String name) {
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

    public static UserStatus get(int id) {
        for(UserStatus userStatus : UserStatus.values()) {
            if(userStatus.getId() == id) {
                return userStatus;
            }
        }
        return null;
    }

}
