package com.autodeploy.responseEntity;

/**
 * @Desc 统一相应实体类
 **/
public class ResultData<T> {

    /**
     * 返回状态码: 如:500
     */
    private Integer code;

    /**
     * 返回信息提示
     */
    private String msg;

    /**
     * 返回处理状态: true 或 false
     */
    private boolean success;

    /**
     * 返回请求的数据
     */
    private T data;


    public ResultData() {}

    public ResultData(Integer code, String msg, boolean success) {
        this.code = code;
        this.msg = msg;
        this.success = success;
    }

    public ResultData(Integer code, String msg, boolean success, T data) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
