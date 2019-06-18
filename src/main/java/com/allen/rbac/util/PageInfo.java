package com.allen.rbac.util;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Map;

public class PageInfo implements Serializable {

    public static int DEFAULT_PAGE_SIZE = 10;

    public static int DEFAULT_CURRENT_PAGE = 1;

    // 每页默认10条数据
    private int pageSize = DEFAULT_PAGE_SIZE;

    // 默认当前页在第一页
    private int currentPage = DEFAULT_CURRENT_PAGE;
    
    // 总页数
    private int totalPages = 0;

    // 总行数
    private int totalRows = 0;

    // 每页的起始行数
    private int pageStartRow = 0;

    // 每页显示数据的终止行数
    private int pageEndRow = 0;

    // 是否分页，默认分页
    private boolean pagination = true;

    // 是否有下一页
    private boolean hasNextPage = false;

    // 是否有前一页
    private boolean hasPreviousPage = false;

    // 请求参数的字符串形式，用于分页组件URL拼接
    private String params;

    // 是否需要统计总数量，默认true
    private boolean needCount = true;

    public PageInfo() {

    }

    public PageInfo(int currentPage) {
        this.currentPage = currentPage;
        this.pagination = true;
    }

    public PageInfo(int totalRows, int pageSize) {
        this.init(totalRows, pageSize);
    }

    /**
     * 初始化分页参数:需要先设置totalRows
     */
    public void init(int totalRows, int pageSize) {
        this.totalRows = totalRows;
        this.pageSize = pageSize;

        totalPages = totalRows / pageSize;
        if ((totalRows % pageSize) != 0) {
            totalPages++;
        }
    }

    public void init(int totalRows, int pageSize, int currentPage) {
        init(totalRows, pageSize);

        if (currentPage != 0)
            gotoPage(currentPage);
    }

    /**
     * 直接跳转到指定页数的页面
     *
     * @param page
     */
    public void gotoPage(int page) {
        if (currentPage > totalPages && currentPage > 1) {
            currentPage = Math.max(1, totalPages); // 当前页最小为1
        } else {
            currentPage = Math.max(1, page); // 当前页最小为1
        }
        calculatePage();
    }

    /**
     * 计算当前页的取值范围：pageStartRow和pageEndRow
     */
    private void calculatePage() {
        hasPreviousPage = (currentPage - 1) > 0;
        hasNextPage = currentPage < totalPages;

        if (currentPage * pageSize < totalRows) { // 判断是否为最后一页
            pageEndRow = currentPage * pageSize;
            pageStartRow = pageEndRow - pageSize;
        } else {
            pageEndRow = totalRows;
            pageStartRow = pageSize * (totalPages - 1);
        }
    }

    public void putTo(Map<String, Object> map) {
        map.put("page", this);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public int getPageEndRow() {
        return pageEndRow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalPages(int i) {
        totalPages = i;
    }

    public void setCurrentPage(int i) {
        currentPage = i;
    }

    public void setHasNextPage(boolean b) {
        hasNextPage = b;
    }

    public void setHasPreviousPage(boolean b) {
        hasPreviousPage = b;
    }

    public void setPageEndRow(int i) {
        pageEndRow = i;
    }

    public PageInfo setPageSize(int i) {
        pageSize = i;
        return this;
    }

    public void setPageStartRow(int i) {
        pageStartRow = i;
    }

    public void setTotalRows(int i) {
        totalRows = i;
    }

    public boolean isPagination() {
        return pagination;
    }

    public PageInfo setPagination(boolean pagination) {
        this.pagination = pagination;
        return this;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public boolean isNeedCount() {
        return needCount;
    }

    /**
     * <pre>
     * 是否需要统计总记录数,默认为true.
     * 如果设置为false，将不会执行count sql，同时，pageInfo中的totalRows、totalPages 将会为0，hasNextPage也将一直为false
     *
     * </pre>
     *
     * @param needCount 默认为true
     * @date 2016年9月8日
     * @author Ternence
     */
    public void setNeedCount(boolean needCount) {
        this.needCount = needCount;
    }

    /**
     * 构建 PageInfo 对象
     *
     * @param pageNumber 当前页码
     * @param pageSize   每页数量
     * @return
     */
    public static PageInfo build(int pageSize, int pageNumber) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPagination(true);
        return pageInfo;
    }

    public boolean isFirstPage(){
        return  this.currentPage == 1;
    }
}
