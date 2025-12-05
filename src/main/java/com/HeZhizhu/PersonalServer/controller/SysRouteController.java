package com.HeZhizhu.PersonalServer.controller;

import com.HeZhizhu.PersonalServer.dto.routeDTO.RouteRequest;
import com.HeZhizhu.PersonalServer.dto.routeDTO.RouteResponse;
import com.HeZhizhu.PersonalServer.entity.SysRoute;
import com.HeZhizhu.PersonalServer.service.SysRouteService;
import com.HeZhizhu.PersonalServer.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 路由管理控制器
 */
@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
@Tag(name = "路由管理", description = "系统路由/菜单CRUD接口")
public class SysRouteController {

    private final SysRouteService sysRouteService;

    /**
     * 获取路由树（启用的路由，前端动态路由使用）
     */
    @GetMapping("/tree")
    @Operation(summary = "获取路由树", description = "获取所有启用的路由，树形结构，供前端动态路由使用")
    public ApiResponse<List<RouteResponse>> getRouteTree() {
        List<RouteResponse> routeTree = sysRouteService.getRouteTree();
        return ApiResponse.success(routeTree);
    }

    /**
     * 获取所有路由树（包含禁用的，后台管理使用）
     */
    @GetMapping("/allTree")
    @Operation(summary = "获取所有路由树", description = "获取所有路由，包含禁用的，树形结构，供后台管理使用")
    public ApiResponse<List<RouteResponse>> getAllRouteTree() {
        List<RouteResponse> routeTree = sysRouteService.getAllRouteTree();
        return ApiResponse.success(routeTree);
    }

    /**
     * 获取所有路由列表（平铺）
     */
    @GetMapping("/list")
    @Operation(summary = "获取路由列表", description = "获取所有路由，平铺列表结构")
    public ApiResponse<List<RouteResponse>> getAllRoutes() {
        List<SysRoute> routes = sysRouteService.findAll();
        List<RouteResponse> responseList = routes.stream()
                .map(sysRouteService::convertToResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(responseList);
    }

    /**
     * 根据ID获取路由详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取路由详情", description = "根据ID获取单个路由的详细信息")
    public ApiResponse<RouteResponse> getRouteById(@PathVariable Long id) {
        return sysRouteService.findById(id)
                .map(route -> ApiResponse.success(sysRouteService.convertToResponse(route)))
                .orElse(ApiResponse.error("路由不存在"));
    }

    /**
     * 新增路由
     */
    @PostMapping
    @Operation(summary = "新增路由", description = "创建新的路由/菜单")
    public ApiResponse<RouteResponse> createRoute(@Valid @RequestBody RouteRequest request) {
        // 检查路由名称是否已存在
        if (sysRouteService.existsByName(request.getName())) {
            return ApiResponse.error("路由名称已存在");
        }

        SysRoute route = sysRouteService.create(request);
        return ApiResponse.success("路由创建成功", sysRouteService.convertToResponse(route));
    }

    /**
     * 更新路由
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新路由", description = "根据ID更新路由信息")
    public ApiResponse<RouteResponse> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteRequest request) {
        // 检查路由名称是否已存在（排除当前ID）
        if (sysRouteService.existsByNameAndIdNot(request.getName(), id)) {
            return ApiResponse.error("路由名称已存在");
        }

        try {
            SysRoute route = sysRouteService.update(id, request);
            return ApiResponse.success("路由更新成功", sysRouteService.convertToResponse(route));
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 删除路由
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除路由", description = "根据ID删除路由（如有子路由需先删除子路由）")
    public ApiResponse<String> deleteRoute(@PathVariable Long id) {
        if (sysRouteService.findById(id).isEmpty()) {
            return ApiResponse.error("路由不存在");
        }

        try {
            sysRouteService.deleteById(id);
            return ApiResponse.success("路由删除成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

