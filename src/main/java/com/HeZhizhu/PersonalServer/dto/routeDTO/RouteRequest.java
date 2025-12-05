package com.HeZhizhu.PersonalServer.dto.routeDTO;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 路由请求 DTO
 */
@Data
public class RouteRequest {

    private Long parentId;  // 父级路由ID

    @NotBlank(message = "路由名称不能为空")
    private String name;  // 路由名称

    @NotBlank(message = "路由路径不能为空")
    private String path;  // 路由路径

    private String component;  // 组件路径

    private String redirect;  // 重定向地址

    private String title;  // 菜单标题

    private String icon;  // 菜单图标

    private Integer sortOrder;  // 排序顺序

    private Boolean hidden;  // 是否隐藏菜单

    private Boolean keepAlive;  // 是否缓存组件

    private Boolean alwaysShow;  // 是否总是显示

    private Byte status;  // 状态：0-禁用，1-启用

    private Byte routeType;  // 路由类型：1-目录，2-菜单，3-按钮

    private String permission;  // 权限标识
}

