package com.HeZhizhu.PersonalServer.dto.routeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 路由查询请求 DTO
 */
@Data
@Schema(description = "路由查询参数")
public class RouteQueryRequest {

    @Schema(description = "路由名称（模糊查询）")
    private String name;

    @Schema(description = "菜单标题（模糊查询）")
    private String title;

    @Schema(description = "路由路径（模糊查询）")
    private String path;

    @Schema(description = "状态：0-禁用，1-启用")
    private Byte status;

    @Schema(description = "路由类型：1-目录，2-菜单，3-按钮")
    private Byte routeType;

    @Schema(description = "父级路由ID")
    private Long parentId;
}


