package com.HeZhizhu.PersonalServer.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

/**
 * 系统路由实体类：对应数据库 sys_route 表
 */
@Data
@Entity
@Table(name = "sys_route")
public class SysRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 路由ID

    @Column(name = "parent_id")
    private Long parentId;  // 父级路由ID，顶级路由为0

    @Column(name = "name", nullable = false, length = 100)
    private String name;  // 路由名称（唯一标识）

    @Column(name = "path", nullable = false, length = 200)
    private String path;  // 路由路径

    @Column(name = "component", length = 200)
    private String component;  // 组件路径

    @Column(name = "redirect", length = 200)
    private String redirect;  // 重定向地址

    @Column(name = "title", length = 100)
    private String title;  // 菜单标题

    @Column(name = "icon", length = 100)
    private String icon;  // 菜单图标

    @Column(name = "sort_order")
    @ColumnDefault("0")
    private Integer sortOrder;  // 排序顺序

    @Column(name = "hidden")
    @ColumnDefault("0")
    private Boolean hidden;  // 是否隐藏菜单

    @Column(name = "keep_alive")
    @ColumnDefault("0")
    private Boolean keepAlive;  // 是否缓存组件

    @Column(name = "always_show")
    @ColumnDefault("0")
    private Boolean alwaysShow;  // 是否总是显示（即使只有一个子路由）

    @Column(name = "status")
    @ColumnDefault("1")
    private Byte status;  // 状态：0-禁用，1-启用

    @Column(name = "route_type")
    @ColumnDefault("1")
    private Byte routeType;  // 路由类型：1-目录，2-菜单，3-按钮

    @Column(name = "permission", length = 100)
    private String permission;  // 权限标识

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_time", nullable = false, updatable = false)
    private Instant createTime;  // 创建时间

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "update_time", nullable = false)
    private Instant updateTime;  // 更新时间

    @PrePersist
    protected void onCreate() {
        createTime = Instant.now();
        updateTime = Instant.now();
        if (parentId == null) parentId = 0L;
        if (sortOrder == null) sortOrder = 0;
        if (hidden == null) hidden = false;
        if (keepAlive == null) keepAlive = false;
        if (alwaysShow == null) alwaysShow = false;
        if (status == null) status = 1;
        if (routeType == null) routeType = 1;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = Instant.now();
    }
}

