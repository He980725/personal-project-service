package com.HeZhizhu.PersonalServer.service;

import com.HeZhizhu.PersonalServer.dto.routeDTO.RouteQueryRequest;
import com.HeZhizhu.PersonalServer.dto.routeDTO.RouteRequest;
import com.HeZhizhu.PersonalServer.dto.routeDTO.RouteResponse;
import com.HeZhizhu.PersonalServer.entity.SysRoute;
import com.HeZhizhu.PersonalServer.repository.SysRouteRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 路由服务层
 */
@Service
@RequiredArgsConstructor
public class SysRouteService {

    private final SysRouteRepository sysRouteRepository;

    /**
     * 获取所有路由（平铺列表）
     */
    public List<SysRoute> findAll() {
        return sysRouteRepository.findAllByOrderBySortOrderAsc();
    }

    /**
     * 条件查询路由（平铺列表）
     */
    public List<SysRoute> findByCondition(RouteQueryRequest query) {
        Specification<SysRoute> spec = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 路由名称模糊查询
            if (StringUtils.hasText(query.getName())) {
                predicates.add(cb.like(root.get("name"), "%" + query.getName() + "%"));
            }

            // 菜单标题模糊查询
            if (StringUtils.hasText(query.getTitle())) {
                predicates.add(cb.like(root.get("title"), "%" + query.getTitle() + "%"));
            }

            // 路由路径模糊查询
            if (StringUtils.hasText(query.getPath())) {
                predicates.add(cb.like(root.get("path"), "%" + query.getPath() + "%"));
            }

            // 状态精确查询
            if (query.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), query.getStatus()));
            }

            // 路由类型精确查询
            if (query.getRouteType() != null) {
                predicates.add(cb.equal(root.get("routeType"), query.getRouteType()));
            }

            // 父级ID精确查询
            if (query.getParentId() != null) {
                predicates.add(cb.equal(root.get("parentId"), query.getParentId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return sysRouteRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "sortOrder"));
    }

    /**
     * 获取所有启用的路由（树形结构）
     */
    public List<RouteResponse> getRouteTree() {
        List<SysRoute> allRoutes = sysRouteRepository.findByStatusOrderBySortOrderAsc((byte) 1);
        return buildRouteTree(allRoutes, 0L);
    }

    /**
     * 获取所有路由（树形结构，包含禁用的）
     */
    public List<RouteResponse> getAllRouteTree() {
        List<SysRoute> allRoutes = sysRouteRepository.findAllByOrderBySortOrderAsc();
        return buildRouteTree(allRoutes, 0L);
    }

    /**
     * 构建路由树
     */
    private List<RouteResponse> buildRouteTree(List<SysRoute> allRoutes, Long parentId) {
        return allRoutes.stream()
                .filter(route -> route.getParentId().equals(parentId))
                .map(route -> {
                    RouteResponse response = convertToResponse(route);
                    response.setChildren(buildRouteTree(allRoutes, route.getId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询路由
     */
    public Optional<SysRoute> findById(Long id) {
        return sysRouteRepository.findById(id);
    }

    /**
     * 保存路由
     */
    public SysRoute save(SysRoute route) {
        return sysRouteRepository.save(route);
    }

    /**
     * 创建路由
     */
    public SysRoute create(RouteRequest request) {
        SysRoute route = convertToEntity(request);
        return sysRouteRepository.save(route);
    }

    /**
     * 更新路由
     */
    public SysRoute update(Long id, RouteRequest request) {
        return sysRouteRepository.findById(id)
                .map(existingRoute -> {
                    existingRoute.setParentId(request.getParentId() != null ? request.getParentId() : 0L);
                    existingRoute.setName(request.getName());
                    existingRoute.setPath(request.getPath());
                    existingRoute.setComponent(request.getComponent());
                    existingRoute.setRedirect(request.getRedirect());
                    existingRoute.setTitle(request.getTitle());
                    existingRoute.setIcon(request.getIcon());
                    existingRoute.setSortOrder(request.getSortOrder());
                    existingRoute.setHidden(request.getHidden());
                    existingRoute.setKeepAlive(request.getKeepAlive());
                    existingRoute.setAlwaysShow(request.getAlwaysShow());
                    existingRoute.setStatus(request.getStatus());
                    existingRoute.setRouteType(request.getRouteType());
                    existingRoute.setPermission(request.getPermission());
                    return sysRouteRepository.save(existingRoute);
                })
                .orElseThrow(() -> new RuntimeException("路由不存在，ID: " + id));
    }

    /**
     * 删除路由
     */
    public void deleteById(Long id) {
        // 检查是否有子路由
        List<SysRoute> children = sysRouteRepository.findByParentIdOrderBySortOrderAsc(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该路由存在子路由，请先删除子路由");
        }
        sysRouteRepository.deleteById(id);
    }

    /**
     * 检查路由名称是否存在
     */
    public boolean existsByName(String name) {
        return sysRouteRepository.existsByName(name);
    }

    /**
     * 检查路由名称是否存在（排除指定ID）
     */
    public boolean existsByNameAndIdNot(String name, Long id) {
        return sysRouteRepository.existsByNameAndIdNot(name, id);
    }

    /**
     * 将实体转换为响应 DTO
     */
    public RouteResponse convertToResponse(SysRoute route) {
        RouteResponse response = new RouteResponse();
        response.setId(route.getId());
        response.setParentId(route.getParentId());
        response.setName(route.getName());
        response.setPath(route.getPath());
        response.setComponent(route.getComponent());
        response.setRedirect(route.getRedirect());
        response.setSortOrder(route.getSortOrder());
        response.setStatus(route.getStatus());
        response.setRouteType(route.getRouteType());
        response.setPermission(route.getPermission());
        response.setCreateTime(route.getCreateTime());
        response.setUpdateTime(route.getUpdateTime());

        // 设置 meta 信息
        RouteResponse.RouteMeta meta = new RouteResponse.RouteMeta();
        meta.setTitle(route.getTitle());
        meta.setIcon(route.getIcon());
        meta.setHidden(route.getHidden());
        meta.setKeepAlive(route.getKeepAlive());
        meta.setAlwaysShow(route.getAlwaysShow());
        response.setMeta(meta);

        response.setChildren(new ArrayList<>());
        return response;
    }

    /**
     * 将请求 DTO 转换为实体
     */
    public SysRoute convertToEntity(RouteRequest request) {
        SysRoute route = new SysRoute();
        route.setParentId(request.getParentId() != null ? request.getParentId() : 0L);
        route.setName(request.getName());
        route.setPath(request.getPath());
        route.setComponent(request.getComponent());
        route.setRedirect(request.getRedirect());
        route.setTitle(request.getTitle());
        route.setIcon(request.getIcon());
        route.setSortOrder(request.getSortOrder());
        route.setHidden(request.getHidden());
        route.setKeepAlive(request.getKeepAlive());
        route.setAlwaysShow(request.getAlwaysShow());
        route.setStatus(request.getStatus());
        route.setRouteType(request.getRouteType());
        route.setPermission(request.getPermission());
        return route;
    }
}

