package com.allen.rbac.dto.req;

import com.allen.rbac.dto.BaseDto;

import java.util.List;

/**
 * 修改角色请求参数
 */
public class UpdateRoleRequestDto extends BaseDto {

    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 权限ID列表
     */
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

    public List<Long> getPrivilegeIdList() {
        return privilegeIdList;
    }

    public void setPrivilegeIdList(List<Long> privilegeIdList) {
        this.privilegeIdList = privilegeIdList;
    }
}
