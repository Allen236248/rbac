package com.allen.rbac.dao;

import com.allen.rbac.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysRoleDao {

    int insert(SysRole sysRole);

    SysRole findById(@Param("id") Long id);

    SysRole findByName(@Param("name") String name);

    List<SysRole> findByIdList(@Param("idList") List<Long> idList);

    int update(SysRole sysRole);

    List<SysRole> findByParams(Map<String, Object> params);

}
