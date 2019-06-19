package com.allen.rbac.service.impl;

import com.allen.rbac.dao.SysUserRoleDao;
import com.allen.rbac.dto.SysRoleDto;
import com.allen.rbac.dto.SysUserRoleDto;
import com.allen.rbac.entity.SysUserRole;
import com.allen.rbac.service.SysRoleService;
import com.allen.rbac.service.SysUserRoleService;
import com.allen.rbac.util.BeanUtils;
import com.allen.rbac.util.ServiceAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysRoleService sysRoleService;

    @Transactional
    @Override
    public List<SysUserRoleDto> saveBatch(Long userId, List<Long> roleIdList) {
        if(CollectionUtils.isEmpty(roleIdList)) {
            return Collections.emptyList();
        }
        List<Long> deleteIdList = new ArrayList<>();

        List<SysUserRoleDto> currentSysUserRoleDtoList = findByUserId(userId);
        for(SysUserRoleDto sysUserRoleDto : currentSysUserRoleDtoList) {
            Long roleId = sysUserRoleDto.getRoleId();
            if(roleIdList.contains(roleId)) {
                //如果包含，无需再保存，则从待保存列表中删除
                roleIdList.remove(roleId);
            } else {
                //原本设置了，但本次保存没有，需要进行删除
                deleteIdList.add(sysUserRoleDto.getId());
            }
        }

        //清洗roleIdList，过滤掉重复的、无效的
        List<Long> cleanRoleIdList = new ArrayList<>();
        List<SysRoleDto> sysRoleDtoList = sysRoleService.findByIdList(roleIdList);
        for(SysRoleDto sysRoleDto : sysRoleDtoList) {
            Long id = sysRoleDto.getId();
            if(roleIdList.contains(id)) {
                cleanRoleIdList.add(id);
            }
        }

        deleteBatch(deleteIdList);

        List<SysUserRoleDto> sysUserRoleDtoList = new ArrayList<>();
        for(Long roleId : cleanRoleIdList) {
            SysUserRoleDto sysUserRoleDto = new SysUserRoleDto();
            sysUserRoleDto.setUserId(userId);
            sysUserRoleDto.setRoleId(roleId);
            sysUserRoleDto.setCreateTime(new Date());
            sysUserRoleDto.setUpdateTime(new Date());
            sysUserRoleDtoList.add(sysUserRoleDto);
        }
        sysUserRoleDao.saveBatch(BeanUtils.copyProperties(sysUserRoleDtoList, SysUserRole.class));
        return sysUserRoleDtoList;
    }

    @Override
    public List<SysUserRoleDto> findByUserId(Long userId) {
        ServiceAssert.assertThat(null == userId || userId < 1, "用户ID无效");

        List<SysUserRole> sysUserRoleList = sysUserRoleDao.findByUserId(userId);
        return BeanUtils.copyProperties(sysUserRoleList, SysUserRoleDto.class);
    }

    @Override
    public List<SysUserRoleDto> findByRoleId(Long roleId) {
        ServiceAssert.assertThat(null == roleId || roleId < 1, "角色ID无效");

        List<SysUserRole> sysUserRoleList = sysUserRoleDao.findByRoleId(roleId);
        return BeanUtils.copyProperties(sysUserRoleList, SysUserRoleDto.class);
    }

    @Override
    public void deleteBatch(List<Long> idList) {
        if(!CollectionUtils.isEmpty(idList)) {
            sysUserRoleDao.deleteBatch(idList);
        }
    }

}
