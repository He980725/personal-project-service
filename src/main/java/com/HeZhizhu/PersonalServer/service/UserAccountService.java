package com.HeZhizhu.PersonalServer.service;

import com.HeZhizhu.PersonalServer.dto.userAccountDTO.UserResponse;
import com.HeZhizhu.PersonalServer.dto.userAccountDTO.UserRequest;
import com.HeZhizhu.PersonalServer.entity.UserAccount;
import com.HeZhizhu.PersonalServer.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用户账户服务层
 */
@Service
@RequiredArgsConstructor
public class UserAccountService {
    
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取所有用户
     */
    public List<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }

    /**
     * 分页查询用户
     */
    public Page<UserAccount> findAll(Pageable pageable) {
        return userAccountRepository.findAll(pageable);
    }

    /**
     * 根据ID查询用户
     */
    public Optional<UserAccount> findById(Long id) {
        return userAccountRepository.findById(id);
    }

    /**
     * 根据用户名查询用户
     */
    public UserAccount findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }

    /**
     * 保存用户（如果密码未加密则加密）
     */
    public UserAccount save(UserAccount userAccount) {
        // 检查密码是否已加密（BCrypt 加密的密码以 $2a$ 或 $2b$ 开头）
        String password = userAccount.getPassword();
        if (password != null && !password.startsWith("$2a$") && !password.startsWith("$2b$")) {
            userAccount.setPassword(passwordEncoder.encode(password));
        }
        return userAccountRepository.save(userAccount);
    }

    /**
     * 更新用户信息
     */
    public UserAccount update(Long id, UserAccount userAccountDetails) {
        return userAccountRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(userAccountDetails.getUsername());
                    existingUser.setPassword(userAccountDetails.getPassword());
                    existingUser.setPhone(userAccountDetails.getPhone());
                    existingUser.setEmail(userAccountDetails.getEmail());
                    existingUser.setAvatar(userAccountDetails.getAvatar());
                    existingUser.setNickname(userAccountDetails.getNickname());
                    existingUser.setGender(userAccountDetails.getGender());
                    existingUser.setStatus(userAccountDetails.getStatus());
                    existingUser.setRole(userAccountDetails.getRole());
                    existingUser.setLastLoginTime(userAccountDetails.getLastLoginTime());
                    existingUser.setLastLoginIp(userAccountDetails.getLastLoginIp());
                    return userAccountRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + id));
    }

    /**
     * 删除用户
     */
    public void deleteById(Long id) {
        userAccountRepository.deleteById(id);
    }

    /**
     * 检查用户名是否存在
     */
    public boolean existsByUsername(String username) {
        return userAccountRepository.findByUsername(username) != null;
    }

    /**
     * 检查邮箱是否存在
     */
    public boolean existsByEmail(String email) {
        return userAccountRepository.findAll().stream()
                .anyMatch(user -> email.equals(user.getEmail()));
    }

    /**
     * 获取用户数量
     */
    public long count() {
        return userAccountRepository.count();
    }

    /**
     * 将 UserAccount 实体转换为 UserResponse DTO
     */
    public UserResponse convertToResponse(UserAccount userAccount) {
        UserResponse response = new UserResponse();
        response.setId(userAccount.getId());
        response.setUsername(userAccount.getUsername());
        response.setPhone(userAccount.getPhone());
        response.setEmail(userAccount.getEmail());
        response.setAvatar(userAccount.getAvatar());
        response.setNickname(userAccount.getNickname());
        response.setGender(userAccount.getGender());
        response.setStatus(userAccount.getStatus());
        response.setRole(userAccount.getRole());
        response.setLastLoginTime(userAccount.getLastLoginTime());
        response.setLastLoginIp(userAccount.getLastLoginIp());
        response.setCreateTime(userAccount.getCreateTime());
        response.setUpdateTime(userAccount.getUpdateTime());
        return response;
    }

    /**
     * 将 UserRequest DTO 转换为 UserAccount 实体
     */
    public UserAccount convertToEntity(UserRequest userRequest) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(userRequest.getUsername());
        String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());
        userAccount.setPassword(encryptedPassword); // 存入加密后的密码
        userAccount.setPhone(userRequest.getPhone());
        userAccount.setEmail(userRequest.getEmail());
        userAccount.setAvatar(userRequest.getAvatar());
        userAccount.setNickname(userRequest.getNickname());
        userAccount.setGender(userRequest.getGender());
        userAccount.setStatus(userRequest.getStatus());
        userAccount.setRole(userRequest.getRole());
        return userAccount;
    }
}
