package com.allen.rbac.dao;

import com.allen.rbac.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysUserRoleDao {

    int insertBatch(@Param("sysUserRoleList") List<SysUserRole> sysUserRoleList);

    void deleteBatch(@Param("idList") List<Long> idList);

    List<SysUserRole> findByUserId(@Param("userId") Long userId);

    List<SysUserRole> findByRoleId(@Param("roleId") Long roleId);

}
