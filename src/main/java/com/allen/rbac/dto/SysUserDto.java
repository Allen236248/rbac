package com.allen.rbac.dto;

import java.util.List;

/**
 * 用户
 */
public class SysUserDto extends BaseDto {

    private Long id;

    /**
     * 登陆用户名
     */
    private String username;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户状态
     * 1：正常
     * 2：用户被锁定
     * 3：用户被删除
     */
    private Integer status;

    private List<SysRoleDto> roleList;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SysRoleDto> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRoleDto> roleList) {
        this.roleList = roleList;
    }

    public List<Long> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
