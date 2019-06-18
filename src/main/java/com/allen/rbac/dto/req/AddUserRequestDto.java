package com.allen.rbac.dto.req;

import com.allen.rbac.dto.BaseDto;
import com.allen.rbac.dto.SysRoleDto;

import java.util.List;

/**
 * 新增用户请求参数
 */
public class AddUserRequestDto extends BaseDto {

    /**
     * 登陆用户名
     */
    private String username;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<Long> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
