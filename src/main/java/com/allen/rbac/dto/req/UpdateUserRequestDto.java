package com.allen.rbac.dto.req;

import com.allen.rbac.dto.BaseDto;

import java.util.List;

/**
 * 修改用户请求参数
 */
public class UpdateUserRequestDto extends BaseDto {

    private Long id;

    /**
     * 登陆用户名
     */
    private String username;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
