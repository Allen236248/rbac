package com.allen.rbac.dao;

import com.allen.rbac.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysUserDao {

    int insert(SysUser sysUser);

    SysUser findById(@Param("id") Long id);

    SysUser findByUsername(@Param("username") String username);

    SysUser findByMobile(@Param("mobile") String mobile);

    List<SysUser> findByParams(Map<String, Object> params);

    int deleteById(@Param("id") Long id);

    int update(SysUser sysUser);

}
