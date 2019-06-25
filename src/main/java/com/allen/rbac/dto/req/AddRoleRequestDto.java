package com.allen.rbac.dto.req;

import com.allen.rbac.dto.BaseDto;

import java.util.List;

/**
 * 新增角色请求参数
 */
public class AddRoleRequestDto extends BaseDto {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色码
     */
    private String code;

    /**
     * 权限ID列表
     */
    private List<Long> privilegeIdList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Long> getPrivilegeIdList() {
        return privilegeIdList;
    }

    public void setPrivilegeIdList(List<Long> privilegeIdList) {
        this.privilegeIdList = privilegeIdList;
    }
}
