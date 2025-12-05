package com.HeZhizhu.PersonalServer.dto.userAccountDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private Long id;          // 用户ID
    private String username;  // 用户名
    private String nickname;  // 昵称
    private String token;     // JWT Token
}

