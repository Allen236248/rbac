package com.allen.rbac.dto.req;

import com.allen.rbac.dto.BaseDto;

/**
 * 分页查询请求参数
 */
public class PaginationRequestDto extends BaseDto {

    /**
     * 当前第几页
     */
    private Integer pageNumber;

    /**
     * 每页多少行数据
     */
    private Integer pageSize;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
