package com.allen.rbac.service;

import com.allen.rbac.dto.SysPrivilegeDto;

import java.util.List;

public interface SysPrivilegeService {

    SysPrivilegeDto addSysPrivilege(SysPrivilegeDto sysPrivilegeDto);

    SysPrivilegeDto findById(Long id);

    List<SysPrivilegeDto> findByPid(Long pid);

    SysPrivilegeDto findByName(String name);

    List<SysPrivilegeDto> findByIdList(List<Long> idList);

    void disableSysPrivilege(Long id);

    SysPrivilegeDto updateSysPrivilege(SysPrivilegeDto sysPrivilegeDto);

    List<SysPrivilegeDto> findAll();

}
