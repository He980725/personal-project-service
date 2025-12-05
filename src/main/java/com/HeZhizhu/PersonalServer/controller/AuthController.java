package com.HeZhizhu.PersonalServer.controller;

import com.HeZhizhu.PersonalServer.dto.userAccountDTO.LoginRequest;
import com.HeZhizhu.PersonalServer.dto.userAccountDTO.LoginResponse;
import com.HeZhizhu.PersonalServer.dto.userAccountDTO.RegisterRequest;
import com.HeZhizhu.PersonalServer.entity.UserAccount;
import com.HeZhizhu.PersonalServer.service.UserAccountService;
import com.HeZhizhu.PersonalServer.util.ApiResponse;
import com.HeZhizhu.PersonalServer.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户登录、注册、token刷新等接口")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserAccountService userAccountService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.cookie-name:AUTH_TOKEN}")
    private String cookieName;

    /**
     * 用户登录 - Token 自动存储到 Cookie
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码登录，返回用户信息和Token")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(loginRequest.getUsername());

        // 将 Token 存储到 Cookie
        addTokenCookie(response, jwt);

        // 获取用户信息
        UserAccount user = userAccountService.findByUsername(loginRequest.getUsername());
        
        // 构建登录响应
        LoginResponse loginResponse = LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .token(jwt)
                .build();

        return ApiResponse.success("登录成功", loginResponse);
    }

    /**
     * 将 Token 添加到 Cookie
     */
    private void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(false);     // 允许 JavaScript 访问（如需更高安全性可设为 true）
        cookie.setSecure(false);       // 生产环境设为 true（仅 HTTPS）
        cookie.setPath("/");           // Cookie 作用路径
        cookie.setMaxAge(jwtExpiration.intValue()); // 过期时间（秒）
        response.addCookie(cookie);
    }

    /**
     * 清除 Token Cookie
     */
    private void clearTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);  // 立即过期
        response.addCookie(cookie);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户账号")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userAccountService.existsByUsername(registerRequest.getUsername())) {
            return ApiResponse.error("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (registerRequest.getEmail() != null && userAccountService.existsByEmail(registerRequest.getEmail())) {
            return ApiResponse.error("邮箱已存在");
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(registerRequest.getUsername());
        userAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // 密码加密
        userAccount.setEmail(registerRequest.getEmail());
        userAccount.setNickname(registerRequest.getNickname());
        userAccount.setPhone(registerRequest.getPhone());

        userAccountService.save(userAccount);

        return ApiResponse.success("注册成功");
    }

    /**
     * 刷新 token - 同时更新 Cookie
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "刷新token", description = "使用当前token刷新获取新token，同时更新Cookie")
    public ApiResponse<String> refreshToken(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String newToken = jwtUtil.generateToken(username);

        // 更新 Cookie 中的 Token
        addTokenCookie(response, newToken);

        return ApiResponse.success("token刷新成功", newToken);
    }

    /**
     * 用户登出 - 清除 Cookie
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出，自动清除Cookie中的token")
    public ApiResponse<String> logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        // 清除 Cookie
        clearTokenCookie(response);
        return ApiResponse.success("登出成功");
    }
}
