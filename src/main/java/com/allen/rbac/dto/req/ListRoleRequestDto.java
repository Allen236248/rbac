package com.allen.rbac.dto.req;

/**
 * 查询角色请求参数
 */
public class ListRoleRequestDto extends PaginationRequestDto {

    /**
     * 角色名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
