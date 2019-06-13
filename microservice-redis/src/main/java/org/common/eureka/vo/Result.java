package org.common.eureka.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    private boolean success = true;

    private int code;

    private String message = "操作成功";

    private T data;

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    public static Result ok(String msg) {
        Result result = new Result();
        result.setMessage(msg);
        return result;
    }

    public static Result ok() {
        Result result = new Result();
        return result;
    }

    public static Result failure(int code, String msg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        if (StringUtils.isEmpty(msg)) {
            msg = "操作失败";
        }
        result.setMessage(msg);
        return result;
    }

    public static Result failure() {
        return failure(null);
    }

    public static Result failure(String msg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(-400);
        if (StringUtils.isEmpty(msg)) {
            msg = "操作失败";
        }
        result.setMessage(msg);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }
}
