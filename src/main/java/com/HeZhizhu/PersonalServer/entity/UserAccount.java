package com.HeZhizhu.PersonalServer.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.ZoneId;

/**
 * 用户实体类：对应数据库 user_account 表
 */
@Data  // Lombok 注解，自动生成 getter/setter/toString 等方法
@Entity  // 标识为 JPA 实体类
@Table(name = "user_account")  // 对应数据库表名（可自定义）
public class UserAccount {
    @Id  // 主键标识
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增主键（MySQL 支持）
    private Long id;  // 用户ID

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "avatar", length = 100)
    private String avatar;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @ColumnDefault("1")
    @Column(name = "gender", nullable = false)
    private Byte gender;

    @ColumnDefault("1")
    @Column(name = "status", nullable = false)
    private Byte status;

    @ColumnDefault("0")
    @Column(name = "role", nullable = false)
    private Byte role;

    @Column(name = "last_login_time")
    private Instant lastLoginTime;

    @Column(name = "last_login_ip", length = 50)
    private String lastLoginIp;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    @ColumnDefault("0")
    @Column(name = "is_delete", nullable = false)
    private String isDelete;

    /**
     * 首次保存（insert）前执行：设置 create_time、is_delete 的默认值
     */
    @PrePersist
    public void prePersist() {
        // 设置创建时间：当前时间（Asia/Shanghai 时区，避免时间偏移）
        this.createTime = Instant.now().atZone(ZoneId.of("Asia/Shanghai")).toInstant();
        // 设置默认未删除："0"（与 @ColumnDefault("0") 一致）
        this.isDelete = "0";
        // 初始化更新时间（与创建时间一致）
        this.updateTime = this.createTime;
    }

    /**
     * 每次更新（update）前执行：刷新 update_time 为当前时间
     */
    @PreUpdate
    public void preUpdate() {
        // 更新时间：刷新为当前时间（Asia/Shanghai 时区）
        this.updateTime = Instant.now().atZone(ZoneId.of("Asia/Shanghai")).toInstant();
    }

}
