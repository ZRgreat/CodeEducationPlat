package sdu.codeeducationplat.common;

import lombok.Data;
import sdu.codeeducationplat.model.enums.ResultCode;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    // 成功响应
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getDefaultMsg());
        result.setData(data);
        return result;
    }

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 错误响应（无数据）
    public static <T> Result<T> error(ResultCode resultCode, String msg) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMsg(msg);
        result.setData(null); // data 置为 null
        return result;
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        return error(resultCode, resultCode.getDefaultMsg());
    }

    public static <T> Result<T> error(String msg) {
        return error(ResultCode.SERVER_ERROR, msg);
    }
}