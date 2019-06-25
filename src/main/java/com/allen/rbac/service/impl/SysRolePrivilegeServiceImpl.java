package com.allen.rbac.service.impl;

import com.allen.rbac.dao.SysRolePrivilegeDao;
import com.allen.rbac.dto.SysPrivilegeDto;
import com.allen.rbac.dto.SysRolePrivilegeDto;
import com.allen.rbac.entity.SysRolePrivilege;
import com.allen.rbac.service.SysPrivilegeService;
import com.allen.rbac.service.SysRolePrivilegeService;
import com.allen.rbac.util.BeanUtils;
import com.allen.rbac.exception.ServiceAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysRolePrivilegeServiceImpl implements SysRolePrivilegeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysRolePrivilegeServiceImpl.class);

    @Autowired
    private SysRolePrivilegeDao sysRolePrivilegeDao;

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    @Transactional
    @Override
    public void saveBatch(Long roleId, List<Long> privilegeIdList) {
        if(CollectionUtils.isEmpty(privilegeIdList)) {
            LOGGER.warn("增加角色权限关系-权限列表为空");
            return;
        }
        List<Long> deleteIdList = new ArrayList<>();

        List<SysRolePrivilegeDto> currentSysRolePrivilegeDtoList = findByRoleId(roleId);
        for(SysRolePrivilegeDto sysRolePrivilegeDto : currentSysRolePrivilegeDtoList) {
            Long privilegeId = sysRolePrivilegeDto.getPrivilegeId();
            if(privilegeIdList.contains(privilegeId)) {
                //如果包含，无需再保存，则从待保存列表中删除
                privilegeIdList.remove(privilegeId);
            } else {
                //原本设置了，但本次保存没有，需要进行删除
                deleteIdList.add(sysRolePrivilegeDto.getId());
            }
        }

        //清洗privilegeIdList，过滤掉重复的、无效的
        List<Long> cleanPrivilegeIdList = new ArrayList<>();
        List<SysPrivilegeDto> sysPrivilegeDtoList = sysPrivilegeService.findByIdList(privilegeIdList);
        for(SysPrivilegeDto sysPrivilegeDto : sysPrivilegeDtoList) {
            Long id = sysPrivilegeDto.getId();
            if(privilegeIdList.contains(id)) {
                cleanPrivilegeIdList.add(id);
            }
        }

        deleteBatch(deleteIdList);

        List<SysRolePrivilegeDto> sysRolePrivilegeDtoList = new ArrayList<>();
        for(Long privilegeId : cleanPrivilegeIdList) {
            SysRolePrivilegeDto sysRolePrivilegeDto = new SysRolePrivilegeDto();
            sysRolePrivilegeDto.setRoleId(roleId);
            sysRolePrivilegeDto.setPrivilegeId(privilegeId);
            sysRolePrivilegeDto.setCreateTime(new Date());
            sysRolePrivilegeDto.setUpdateTime(new Date());
            sysRolePrivilegeDtoList.add(sysRolePrivilegeDto);
        }
        sysRolePrivilegeDao.insertBatch(BeanUtils.copyProperties(sysRolePrivilegeDtoList, SysRolePrivilege.class));
    }

    @Override
    public List<SysRolePrivilegeDto> findByRoleId(Long roleId) {
        ServiceAssert.assertThat(null == roleId || roleId < 1, "角色ID无效");

        List<SysRolePrivilege> sysRolePrivilegeList = sysRolePrivilegeDao.findByRoleId(roleId);
        return BeanUtils.copyProperties(sysRolePrivilegeList, SysRolePrivilegeDto.class);
    }

    @Override
    public void deleteBatch(List<Long> idList) {
        if(!CollectionUtils.isEmpty(idList)) {
            sysRolePrivilegeDao.deleteBatch(idList);
        }
    }

    @Override
    public List<SysRolePrivilegeDto> findByPrivilegeId(Long privilegeId) {
        ServiceAssert.assertThat(null == privilegeId || privilegeId < 1, "权限ID无效");

        List<SysRolePrivilege> sysRolePrivilegeList = sysRolePrivilegeDao.findByPrivilegeId(privilegeId);
        return BeanUtils.copyProperties(sysRolePrivilegeList, SysRolePrivilegeDto.class);
    }

}
