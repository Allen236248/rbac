package com.allen.rbac.entity;

import com.allen.rbac.enums.PrivilegePlatform;

/**
 * 权限
 */
public class SysPrivilege extends BaseEntity {

    private Long id;

    /**
     * 父节点ID
     */
    private Long pid;

    /**
     * 权限使用的平台
     * @see PrivilegePlatform
     */
    private Integer platform;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限码
     */
    private String code;

    /**
     * 权限类别
     * menu：菜单
     * operation：操作（查看/增加/删除/修改）
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
     * 权限状态
     * 0：禁用
     * 1：启用
     */
    private Integer status;

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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
