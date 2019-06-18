package com.allen.rbac.dto.req;

/**
 * 查询用户请求参数
 */
public class ListUserRequestDto extends PaginationRequestDto {

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 角色ID
     */
    private Long roleId;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
