package com.pr0xs1.campusfix.pojo;

import lombok.Data;

@Data
public class Result {
    /** 业务状态码  成功1 失败0 */
    private Integer code;
    /** 错误信息 */
    private String msg;
    /** 数据 */
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(1);
        result.setMsg("success");
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(1);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }
}
