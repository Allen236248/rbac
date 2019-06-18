package com.allen.rbac.entity;

import java.util.List;

/**
 * 角色
 */
public class SysRole extends BaseEntity {

    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色状态
     * 0：禁用
     * 1：启用
     */
    private Integer status;

    /**
     * 权限列表
     */
    private List<SysPrivilege> privilegeList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SysPrivilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<SysPrivilege> privilegeList) {
        this.privilegeList = privilegeList;
    }
}
