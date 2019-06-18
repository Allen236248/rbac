package com.allen.rbac.dao;

import com.allen.rbac.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleDao {

    int saveBatch(List<SysUserRole> sysUserRoleList);

    void deleteBatch(List<Long> idList);

    List<SysUserRole> findByUserId(@Param("userId") Long userId);

}
