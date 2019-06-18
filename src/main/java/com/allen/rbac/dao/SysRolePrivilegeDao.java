package com.allen.rbac.dao;

import com.allen.rbac.entity.SysRolePrivilege;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysRolePrivilegeDao {

    int saveBatch(List<SysRolePrivilege> sysRolePrivilegeList);

    void deleteBatch(List<Long> idList);

    List<SysRolePrivilege> findByRoleId(@Param("roleId") Long roleId);

    List<SysRolePrivilege> findByPrivilegeId(@Param("privilegeId") Long privilegeId);

}
