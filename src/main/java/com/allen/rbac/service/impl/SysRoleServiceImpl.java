package com.allen.rbac.service.impl;

import com.allen.rbac.dao.SysRoleDao;
import com.allen.rbac.dto.SysRoleDto;
import com.allen.rbac.entity.SysRole;
import com.allen.rbac.service.SysRolePrivilegeService;
import com.allen.rbac.service.SysRoleService;
import com.allen.rbac.util.BeanUtils;
import com.allen.rbac.util.PageInfo;
import com.allen.rbac.util.PageResult;
import com.allen.rbac.util.ServiceAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;

    @Transactional
    @Override
    public SysRoleDto addSysRole(SysRoleDto sysRoleDto) {
        String name = sysRoleDto.getName();
        ServiceAssert.assertThat(!StringUtils.hasText(name), "角色名称不能为空");
        //去除左右空格
        name = name.trim();
        ServiceAssert.assertThat(null != findByName(name), "角色名称已存在");

        sysRoleDto.setName(name);
        sysRoleDto.setStatus(1);
        sysRoleDao.insert(BeanUtils.copyProperties(sysRoleDto, SysRole.class));

        Long roleId = sysRoleDto.getId();
        List<Long> privilegeIdList = sysRoleDto.getPrivilegeIdList();
        sysRolePrivilegeService.saveBatch(roleId, privilegeIdList);

        return sysRoleDto;
    }

    @Override
    public SysRoleDto findById(Long id) {
        ServiceAssert.assertThat(null == id || id.longValue() < 1, "角色ID不能为空或小于1");

        SysRole sysRole = sysRoleDao.findById(id);
        return BeanUtils.copyProperties(sysRole, SysRoleDto.class);
    }

    @Override
    public SysRoleDto findByName(String name) {
        ServiceAssert.assertThat(!StringUtils.hasText(name), "角色名称不能为空");

        SysRole sysRole = sysRoleDao.findByName(name);
        return BeanUtils.copyProperties(sysRole, SysRoleDto.class);
    }

    @Override
    public List<SysRoleDto> findByIdList(List<Long> idList) {
        if(CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        List<SysRole> sysRoleList = sysRoleDao.findByIdList(idList);
        return BeanUtils.copyProperties(sysRoleList, SysRoleDto.class);
    }

    @Transactional
    @Override
    public SysRoleDto updateSysRole(SysRoleDto sysRoleDto) {
        Long id = sysRoleDto.getId();
        ServiceAssert.assertThat(null == id || id.longValue() < 1, "角色ID不能为空或小于1");

        SysRoleDto exist = findById(id);
        ServiceAssert.assertThat(null == exist, "角色不存在");

        String name = sysRoleDto.getName();
        if(StringUtils.hasText(name) && !name.equals(exist.getName())) {
            name = name.trim();

            SysRoleDto existOfName = findByName(name);
            ServiceAssert.assertThat(null != existOfName && existOfName.getId().longValue() != id.longValue(), "角色名已存在");
            exist.setName(name);
        }

        sysRoleDao.update(BeanUtils.copyProperties(exist, SysRole.class));

        List<Long> privilegeIdList = sysRoleDto.getPrivilegeIdList();
        sysRolePrivilegeService.saveBatch(id, privilegeIdList);

        return sysRoleDto;
    }

    @Override
    public PageResult<SysRoleDto> findByParams(Map<String, Object> params, PageInfo pageInfo) {
        ServiceAssert.assertThat(null == pageInfo, "分页信息不能为空");

        params.put("page", pageInfo);
        List<SysRole> sysRoleList = sysRoleDao.findByParams(params);
        return PageResult.build(pageInfo, BeanUtils.copyProperties(sysRoleList, SysRoleDto.class));
    }

}
