package com.HeZhizhu.PersonalServer.dto.userAccountDTO;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 注册请求 DTO
 */
@Data
public class RegisterRequest {
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    private String email;
    
    private String phone;
    
    private String nickname;
}
