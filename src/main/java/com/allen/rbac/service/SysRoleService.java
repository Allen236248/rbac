package com.allen.rbac.service;

import com.allen.rbac.dto.SysRoleDto;
import com.allen.rbac.util.PageInfo;
import com.allen.rbac.util.PageResult;

import java.util.List;
import java.util.Map;

public interface SysRoleService {

    SysRoleDto addSysRole(SysRoleDto sysRoleDto);

    SysRoleDto findById(Long id);

    SysRoleDto findByName(String name);

    List<SysRoleDto> findByIdList(List<Long> idList);

    SysRoleDto updateSysRole(SysRoleDto sysRoleDto);

    PageResult<SysRoleDto> findByParams(Map<String, Object> params, PageInfo pageInfo);

}
