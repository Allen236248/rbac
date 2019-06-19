package com.allen.rbac.dto.req;

import com.allen.rbac.dto.BaseDto;

/**
 * 修改权限请求参数
 */
public class UpdatePrivilegeRequestDto extends BaseDto {

    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限类别
     * MENU：菜单
     * OPERATION：操作（查看/增加/删除/修改）
     */
    private String type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 请求的URL地址
     */
    private String url;

    /**
     * 备注
     */
    private String remark;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
