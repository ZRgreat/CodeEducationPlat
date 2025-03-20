package sdu.codeeducationplat.common;

/**
 * 统一 API 返回结果封装类
 * @param <T> 返回数据的类型
 */
public class Result<T> {
    private int code; // 状态码
    private String msg; // 消息
    private T data; // 数据

    // 私有构造函数，防止直接实例化
    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 失败响应（无数据）
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }

    // 失败响应（带数据）
    public static <T> Result<T> error(String msg, T data) {
        return new Result<>(500, msg, data);
    }

    // 设置数据
    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    // Getters
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}