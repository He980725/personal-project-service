package com.HeZhizhu.PersonalServer.util;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * 统一API响应格式
 */
@Data
public class ApiResponse<T> {
    
    private int code;           // 状态码
    private String message;     // 消息
    private T data;             // 数据
    private LocalDateTime timestamp; // 时间戳
    
    /**
     * 私有构造函数
     */
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * 成功响应（无数据）
     */
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(HttpStatus.OK.value(), "操作成功", null);
    }
    
    /**
     * 成功响应（有数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "操作成功", data);
    }
    
    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, data);
    }
    
    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), message, null);
    }
    
    /**
     * 失败响应（自定义状态码）
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
    
    /**
     * 失败响应（HTTP状态）
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return new ApiResponse<>(status.value(), message, null);
    }
    
    /**
     * 未找到资源
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), message, null);
    }
    
    /**
     * 未授权
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), message, null);
    }
    
    /**
     * 禁止访问
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(HttpStatus.FORBIDDEN.value(), message, null);
    }
    
    /**
     * 服务器错误
     */
    public static <T> ApiResponse<T> serverError(String message) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }
}
