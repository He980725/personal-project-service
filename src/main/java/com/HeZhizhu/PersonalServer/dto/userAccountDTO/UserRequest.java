package com.HeZhizhu.PersonalServer.dto.userAccountDTO;

import lombok.Data;

import java.time.Instant;

/**
 * 用户请求 DTO
 */
@Data
public class UserRequest {
    private String username;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private String nickname;
    private Byte gender;
    private Byte status;
    private Byte role;
}
