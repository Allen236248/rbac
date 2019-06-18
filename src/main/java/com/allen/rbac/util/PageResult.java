package com.allen.rbac.util;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {

    private PageInfo pageInfo;

    private List<T> objList;


    public PageResult() {

    }

    public PageResult(PageInfo pageInfo, List<T> objList) {
        this.pageInfo = pageInfo;
        this.objList = objList;
    }

    public static <T> PageResult<T> build(PageInfo pageInfo, List<T> objList) {
        return new PageResult<>(pageInfo, objList);
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<T> getObjList() {
        return objList;
    }

    public void setObjList(List<T> objList) {
        this.objList = objList;
    }
}
