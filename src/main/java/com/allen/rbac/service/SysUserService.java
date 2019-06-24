package com.allen.rbac.service;

import com.allen.rbac.dto.SysUserDto;
import com.allen.rbac.util.PageInfo;
import com.allen.rbac.util.PageResult;

import java.util.Map;

public interface SysUserService {

    SysUserDto addSysUser(SysUserDto sysUserDto);

    SysUserDto findById(Long id);

    SysUserDto findByUsername(String username);

    SysUserDto findByMobile(String mobile);

    PageResult<SysUserDto> findByParams(Map<String, Object> params, PageInfo pageInfo);

    void deleteById(Long id);

    SysUserDto updateSysUser(SysUserDto sysUserDto);

    SysUserDto findByUsernameWithPrivilege(String username);

}
