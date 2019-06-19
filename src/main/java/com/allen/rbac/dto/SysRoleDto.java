package com.allen.rbac.dto;

import java.util.List;

/**
 * 角色
 */
public class SysRoleDto extends BaseDto {

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

    private List<SysPrivilegeDto> privilegeList;

    private List<Long> privilegeIdList;

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

    public List<SysPrivilegeDto> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<SysPrivilegeDto> privilegeList) {
        this.privilegeList = privilegeList;
    }

    public List<Long> getPrivilegeIdList() {
        return privilegeIdList;
    }

    public void setPrivilegeIdList(List<Long> privilegeIdList) {
        this.privilegeIdList = privilegeIdList;
    }
}
