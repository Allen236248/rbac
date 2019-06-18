package com.allen.rbac.enums;

public enum PrivilegePlatform {

    PC_OEM(1, "PC端OEM后台"), PC_MERCHANT(2, "PC端商户后台"), WX(3, "微信公众号"), WXA(4, "微信小程序");

    private int id;

    private String name;

    private PrivilegePlatform(int id, String name) {
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

    public static PrivilegePlatform get(int id) {
        for(PrivilegePlatform privilegeStatus : PrivilegePlatform.values()) {
            if(privilegeStatus.getId() == id) {
                return privilegeStatus;
            }
        }
        return null;
    }

}
