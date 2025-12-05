package com.HeZhizhu.PersonalServer.service;

import com.HeZhizhu.PersonalServer.entity.UserAccount;
import com.HeZhizhu.PersonalServer.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义 UserDetailsService
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        
        if (userAccount == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        return User.builder()
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .roles(getUserRole(userAccount.getRole()))
                .build();
    }

    /**
     * 根据用户角色代码获取角色名称
     */
    private String getUserRole(Byte roleCode) {
        if (roleCode == null) {
            return "USER";
        }
        
        switch (roleCode) {
            case 0: return "ADMIN";
            case 1: return "USER";
            default: return "USER";
        }
    }
}
