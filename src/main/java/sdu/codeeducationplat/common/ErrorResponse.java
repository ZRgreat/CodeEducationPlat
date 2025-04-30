package sdu.codeeducationplat.common;

import lombok.Data;
import sdu.codeeducationplat.model.enums.ResultCode;

import java.util.Map;

@Data
public class ErrorResponse {
    private int code;
    private String message;
    private Map<String, Object> details; // 用于存储详细错误信息，例如参数验证失败的字段

    public static ErrorResponse of(ResultCode resultCode, String message, Map<String, Object> details) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(resultCode.getCode());
        response.setMessage(message);
        response.setDetails(details);
        return response;
    }

    public static ErrorResponse of(ResultCode resultCode, String message) {
        return of(resultCode, message, null);
    }

    public static ErrorResponse of(ResultCode resultCode) {
        return of(resultCode, resultCode.getDefaultMsg(), null);
    }
}