package sdu.codeeducationplat.config;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.model.enums.ResultCode;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex, ServletWebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.warn("参数校验失败，请求路径：{}，方法：{}，参数：{}，错误：{}",
                request.getDescription(false),
                request.getRequest().getMethod(),
                request.getParameterMap(),
                errors);
        return Result.error(ResultCode.BAD_REQUEST, "参数验证失败: " + errors.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, ServletWebRequest request) {
        log.warn("请求体解析失败，请求路径：{}，方法：{}，错误：{}",
                request.getDescription(false),
                request.getRequest().getMethod(),
                ex.getMessage(),
                ex);
        return Result.error(ResultCode.BAD_REQUEST, "请求体格式错误");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException ex, ServletWebRequest request) {
        log.warn("非法参数，请求路径：{}，方法：{}，错误：{}",
                request.getDescription(false),
                request.getRequest().getMethod(),
                ex.getMessage(),
                ex);
        return Result.error(ResultCode.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleJwtException(JwtException ex, ServletWebRequest request) {
        log.warn("JWT 异常，请求路径：{}，方法：{}，错误：{}",
                request.getDescription(false),
                request.getRequest().getMethod(),
                ex.getMessage(),
                ex);
        return Result.error(ResultCode.UNAUTHORIZED, "Token 无效或已过期");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handleAccessDenied(AccessDeniedException ex, ServletWebRequest request) {
        log.warn("权限不足，请求路径：{}，方法：{}，错误：{}",
                request.getDescription(false),
                request.getRequest().getMethod(),
                ex.getMessage(),
                ex);
        return Result.error(ResultCode.FORBIDDEN, "无权访问该资源");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRuntimeException(RuntimeException ex, ServletWebRequest request) {
        log.error("运行时异常，请求路径：{}，方法：{}，错误：",
                request.getDescription(false),
                request.getRequest().getMethod(),
                ex);
        return Result.error(ResultCode.SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleAll(Exception ex, ServletWebRequest request) {
        log.error("未知异常，请求路径：{}，方法：{}，错误：",
                request.getDescription(false),
                request.getRequest().getMethod(),
                ex);
        return Result.error(ResultCode.SERVER_ERROR, "服务器内部错误: " + ex.getMessage());
    }
}