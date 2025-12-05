package com.HeZhizhu.PersonalServer.dto.routeDTO;

import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * 路由响应 DTO
 */
@Data
public class RouteResponse {

    private Long id;  // 路由ID

    private Long parentId;  // 父级路由ID

    private String name;  // 路由名称

    private String path;  // 路由路径

    private String component;  // 组件路径

    private String redirect;  // 重定向地址

    private RouteMeta meta;  // 路由元信息

    private Integer sortOrder;  // 排序顺序

    private Byte status;  // 状态

    private Byte routeType;  // 路由类型

    private String permission;  // 权限标识

    private Instant createTime;  // 创建时间

    private Instant updateTime;  // 更新时间

    private List<RouteResponse> children;  // 子路由

    /**
     * 路由元信息（前端路由 meta 字段）
     */
    @Data
    public static class RouteMeta {
        private String title;      // 菜单标题
        private String icon;       // 菜单图标
        private Boolean hidden;    // 是否隐藏
        private Boolean keepAlive; // 是否缓存
        private Boolean alwaysShow;// 是否总是显示
    }
}

