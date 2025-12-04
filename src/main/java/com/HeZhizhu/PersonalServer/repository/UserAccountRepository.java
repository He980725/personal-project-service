package com.HeZhizhu.PersonalServer.repository;

import com.HeZhizhu.PersonalServer.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问接口：JpaRepository 提供了 CRUD 等基础方法，无需手动实现
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    // JpaRepository<实体类, 主键类型>
    // 可自定义查询方法（JPA 自动解析方法名生成 SQL）
    UserAccount findByUsername(String username);  // 根据用户名查询用户


}
