package com.allen.rbac.util;

import java.io.Serializable;

public class ApiResult<T> implements Serializable {

    private String code = "0";

    private String msg;

    private T attach;

    private ApiResult() {

    }

    public static <T> ApiResult<T> build() {
        return new ApiResult<T>();
    }

    public static <T> ApiResult<T> build(T attach) {
        ApiResult<T> result = new ApiResult<T>();
        result.setAttach(attach);
        return result;
    }

    public ApiResult<T> error(String msg) {
        return error("-1", msg);
    }

    public ApiResult<T> error(String code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getAttach() {
        return attach;
    }

    public void setAttach(T attach) {
        this.attach = attach;
    }
}
