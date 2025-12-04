package com.HeZhizhu.PersonalServer.dto.userAccountDTO;

import lombok.Data;

import java.time.Instant;

/**
 * 用户响应 DTO
 */
@Data
public class UserResponse {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String avatar;
    private String nickname;
    private Byte gender;
    private Byte status;
    private Byte role;
    private Instant lastLoginTime;
    private String lastLoginIp;
    private Instant createTime;
    private Instant updateTime;
}
