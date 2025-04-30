package sdu.codeeducationplat.model.enums;

public enum ResultCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "无权访问"),
    NOT_FOUND(404, "资源未找到"),
    SERVER_ERROR(500, "服务器错误");

    private final int code;
    private final String defaultMsg;

    ResultCode(int code, String defaultMsg) {
        this.code = code;
        this.defaultMsg = defaultMsg;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMsg() {
        return defaultMsg;
    }
}