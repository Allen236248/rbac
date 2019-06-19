package com.allen.rbac.dao;

import com.allen.rbac.entity.SysRolePrivilege;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysRolePrivilegeDao {

    int insertBatch(@Param("sysRolePrivilegeList") List<SysRolePrivilege> sysRolePrivilegeList);

    void deleteBatch(@Param("idList") List<Long> idList);

    List<SysRolePrivilege> findByRoleId(@Param("roleId") Long roleId);

    List<SysRolePrivilege> findByPrivilegeId(@Param("privilegeId") Long privilegeId);

}
