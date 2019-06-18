package com.allen.rbac.dao;

import com.allen.rbac.entity.SysPrivilege;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysPrivilegeDao {

    int insert(SysPrivilege sysPrivilege);

    SysPrivilege findById(@Param("id") Long id);

    List<SysPrivilege> findByPid(@Param("pid") Long pid);

    SysPrivilege findByName(@Param("name") String name);

    List<SysPrivilege> findByIdList(@Param("idList") List<Long> idList);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int update(SysPrivilege sysPrivilege);

    List<SysPrivilege> findAll();

}
