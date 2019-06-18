package com.allen.rbac.service;

import com.allen.rbac.dto.SysRolePrivilegeDto;

import java.util.List;

public interface SysRolePrivilegeService {

    List<SysRolePrivilegeDto> saveBatch(Long roleId, List<Long> privilegeIdList);

    List<SysRolePrivilegeDto> findByRoleId(Long roleId);

    void deleteBatch(List<Long> idList);

    List<SysRolePrivilegeDto> findByPrivilegeId(Long privilegeId);
}
