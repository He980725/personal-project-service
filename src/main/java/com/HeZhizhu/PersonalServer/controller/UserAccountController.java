package com.HeZhizhu.PersonalServer.controller;

import com.HeZhizhu.PersonalServer.dto.userAccountDTO.UserRequest;
import com.HeZhizhu.PersonalServer.dto.userAccountDTO.UserResponse;
import com.HeZhizhu.PersonalServer.entity.UserAccount;
import com.HeZhizhu.PersonalServer.service.UserAccountService;
import com.HeZhizhu.PersonalServer.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户账户控制器
 */
@RestController
@RequestMapping("/userAccount")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户CRUD、查询等接口") // 接口分组标签
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * 获取所有用户（使用统一响应格式）
     */
    @GetMapping("/getAllUsers")
    @Operation(summary = "查询所有用户", description = "返回系统中所有用户的完整信息，无需参数") // 极客时间描述
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<UserAccount> users = userAccountService.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(userAccountService::convertToResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(userResponses);
    }

    /**
     * 分页查询用户（使用统一响应格式）
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "传分页查询用户")
    public ApiResponse<Page<UserResponse>> getUsersByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserAccount> userPage = userAccountService.findAll(pageable);
        Page<UserResponse> responsePage = userPage.map(userAccountService::convertToResponse);
        return ApiResponse.<Page<UserResponse>>success(responsePage);
    }

    /**
     * 根据ID获取用户（使用统一响应格式）
     */
    @GetMapping("getUserDetail/{id}")
    @Operation(summary = "根据ID查询用户", description = "传入用户ID，返回对应用户的详细信息")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        Optional<UserAccount> user = userAccountService.findById(id);
        if (user.isPresent()) {
            return ApiResponse.success(userAccountService.convertToResponse(user.get()));
        } else {
            return ApiResponse.error("用户不存在");
        }
    }

    /**
     * 创建新用户（使用统一响应格式）
     */
    @PostMapping("createNewUserAccount")
    @Operation(summary = "新增用户", description = "传入用户名、密码、昵称等信息，创建新用户")
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        // 检查用户名是否已存在
        if (userAccountService.existsByUsername(userRequest.getUsername())) {
            return ApiResponse.error("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userRequest.getEmail() != null && userAccountService.existsByEmail(userRequest.getEmail())) {
            return ApiResponse.error("邮箱已存在");
        }
        
        UserAccount userAccount = userAccountService.convertToEntity(userRequest);
        UserAccount savedUser = userAccountService.save(userAccount);
        UserResponse response = userAccountService.convertToResponse(savedUser);
        return ApiResponse.success("用户创建成功", response);
    }

    /**
     * 更新用户信息（使用统一响应格式）
     */
    @PostMapping("/update/{id}")
    @Operation(summary = "修改用户信息", description = "传入用户ID和修改后的信息，更新用户数据")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        try {
            UserAccount userAccountDetails = userAccountService.convertToEntity(userRequest);
            UserAccount updatedUser = userAccountService.update(id, userAccountDetails);
            UserResponse response = userAccountService.convertToResponse(updatedUser);
            return ApiResponse.success("用户更新成功", response);
        } catch (RuntimeException e) {
            return ApiResponse.error("用户不存在");
        }
    }

    /**
     * 删除用户（使用统一响应格式）
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "根据ID删除用户", description = "传入用户ID，删除对应用户（不可逆）")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        if (userAccountService.findById(id).isPresent()) {
            userAccountService.deleteById(id);
            return ApiResponse.success("用户删除成功");
        } else {
            return ApiResponse.error("用户不存在");
        }
    }
}
