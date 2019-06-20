package com.allen.rbac.service;

import com.allen.rbac.dto.SysUserRoleDto;

import java.util.List;

public interface SysUserRoleService {

    void saveBatch(Long userId, List<Long> roleIdList);

    List<SysUserRoleDto> findByUserId(Long userId);

    List<SysUserRoleDto> findByRoleId(Long roleId);

    void deleteBatch(List<Long> idList);

}
