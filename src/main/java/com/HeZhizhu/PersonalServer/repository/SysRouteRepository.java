package com.HeZhizhu.PersonalServer.repository;

import com.HeZhizhu.PersonalServer.entity.SysRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 路由数据访问接口
 */
@Repository
public interface SysRouteRepository extends JpaRepository<SysRoute, Long> {

    /**
     * 根据父级ID查询子路由
     */
    List<SysRoute> findByParentIdOrderBySortOrderAsc(Long parentId);

    /**
     * 根据状态查询所有路由
     */
    List<SysRoute> findByStatusOrderBySortOrderAsc(Byte status);

    /**
     * 查询所有路由并按排序字段排序
     */
    List<SysRoute> findAllByOrderBySortOrderAsc();

    /**
     * 根据路由名称查询
     */
    SysRoute findByName(String name);

    /**
     * 检查路由名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查路由名称是否存在（排除指定ID）
     */
    boolean existsByNameAndIdNot(String name, Long id);
}

