package com.allen.rbac.service.impl;

import com.allen.rbac.dao.SysPrivilegeDao;
import com.allen.rbac.dto.SysPrivilegeDto;
import com.allen.rbac.dto.SysRolePrivilegeDto;
import com.allen.rbac.entity.SysPrivilege;
import com.allen.rbac.enums.PrivilegePlatform;
import com.allen.rbac.enums.PrivilegeStatus;
import com.allen.rbac.enums.PrivilegeType;
import com.allen.rbac.service.SysPrivilegeService;
import com.allen.rbac.service.SysRolePrivilegeService;
import com.allen.rbac.util.BeanUtils;
import com.allen.rbac.util.ServiceAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class SysPrivilegeServiceImpl implements SysPrivilegeService {

    @Autowired
    private SysPrivilegeDao sysPrivilegeDao;

    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;

    @Override
    public SysPrivilegeDto addSysPrivilege(SysPrivilegeDto sysPrivilegeDto) {
        Long pid = sysPrivilegeDto.getPid();
        Integer platform = sysPrivilegeDto.getPlatform();
        String name = sysPrivilegeDto.getName();
        String code = sysPrivilegeDto.getCode();
        String type = sysPrivilegeDto.getType();
        Integer sort = sysPrivilegeDto.getSort();
        String url = sysPrivilegeDto.getUrl();
        String remark = sysPrivilegeDto.getRemark();

        ServiceAssert.assertThat(null == pid, "请选择父节点");
        ServiceAssert.assertThat(null == PrivilegePlatform.get(platform), "无效权限平台");
        ServiceAssert.assertThat(!StringUtils.hasText(name), "权限名称不能为空");
        ServiceAssert.assertThat(!StringUtils.hasText(code), "权限码不能为空");
        ServiceAssert.assertThat(null == PrivilegeType.get(type), "无效权限类别");
        ServiceAssert.assertThat(!StringUtils.hasText(url), "请求的URL地址不能为空");
        //顶级菜单添加子菜单不做此校验
        ServiceAssert.assertThat(pid.longValue() != 0 && null == findById(pid), "父节点不存在");
        //去除左右空格
        name = name.trim();
        ServiceAssert.assertThat(null != findByName(name), "权限名称已存在");
        code = code.trim();
        ServiceAssert.assertThat(null != findByCode(code), "权限码已存在");

        List<SysPrivilegeDto> sysPrivilegeDtoList = findByPid(pid);
        if(!CollectionUtils.isEmpty(sysPrivilegeDtoList)) {
            for(SysPrivilegeDto dto : sysPrivilegeDtoList) {
                ServiceAssert.assertThat(dto.getSort().intValue() == sort.intValue(), "相同父节点下已存在排序为" + sort + "的节点");
            }
        }

        sysPrivilegeDto.setName(name);
        sysPrivilegeDto.setCode(code);
        sysPrivilegeDto.setStatus(PrivilegeStatus.ENABLE.getId());
        sysPrivilegeDto.setRemark(remark.trim());

        sysPrivilegeDao.insert(BeanUtils.copyProperties(sysPrivilegeDto, SysPrivilege.class));
        return sysPrivilegeDto;
    }

    @Override
    public SysPrivilegeDto findById(Long id) {
        ServiceAssert.assertThat(null == id || id.longValue() < 1, "权限ID不能为空或小于1");

        SysPrivilege sysPrivilege = sysPrivilegeDao.findById(id);
        return BeanUtils.copyProperties(sysPrivilege, SysPrivilegeDto.class);
    }

    @Override
    public List<SysPrivilegeDto> findByPid(Long pid) {
        if(null == pid || pid.longValue() < 0)
            pid = 0L;
        List<SysPrivilege> sysPrivilegeList = sysPrivilegeDao.findByPid(pid);
        return BeanUtils.copyProperties(sysPrivilegeList, SysPrivilegeDto.class);
    }

    @Override
    public SysPrivilegeDto findByName(String name) {
        ServiceAssert.assertThat(!StringUtils.hasText(name), "权限名称不能为空");

        SysPrivilege sysPrivilege = sysPrivilegeDao.findByName(name);
        return BeanUtils.copyProperties(sysPrivilege, SysPrivilegeDto.class);
    }

    @Override
    public SysPrivilegeDto findByCode(String code) {
        ServiceAssert.assertThat(!StringUtils.hasText(code), "权限码不能为空");

        SysPrivilege sysPrivilege = sysPrivilegeDao.findByCode(code);
        return BeanUtils.copyProperties(sysPrivilege, SysPrivilegeDto.class);
    }

    @Override
    public List<SysPrivilegeDto> findByIdList(List<Long> idList) {
        if(CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        List<SysPrivilege> sysPrivilegeList = sysPrivilegeDao.findByIdList(idList);
        return BeanUtils.copyProperties(sysPrivilegeList, SysPrivilegeDto.class);
    }

    @Override
    public void disableSysPrivilege(Long id) {
        ServiceAssert.assertThat(null == id && id.longValue() < 1, "权限ID不能为空或小于1");

        //如果是父节点，存在自节点，不能删除
        ServiceAssert.assertThat(!CollectionUtils.isEmpty(findByPid(id)), "当前节点存在自节点，不能删除");
        //如果有关联角色，不能删除
        List<SysRolePrivilegeDto> sysRolePrivilegeDtoList = sysRolePrivilegeService.findByPrivilegeId(id);
        ServiceAssert.assertThat(!CollectionUtils.isEmpty(sysRolePrivilegeDtoList), "当前节点关联角色，不能删除");

        sysPrivilegeDao.disableSysPrivilege(id, PrivilegeStatus.DISABLE.getId());
    }

    @Override
    public SysPrivilegeDto updateSysPrivilege(SysPrivilegeDto sysPrivilegeDto) {
        Long id = sysPrivilegeDto.getId();
        ServiceAssert.assertThat(null == id || id.longValue() < 1, "权限ID不能为空或小于1");

        SysPrivilegeDto exist = findById(id);
        ServiceAssert.assertThat(null == exist, "权限不存在");

        String name = sysPrivilegeDto.getName();
        String type = sysPrivilegeDto.getType();
        Integer sort = sysPrivilegeDto.getSort();
        String url = sysPrivilegeDto.getUrl();
        String remark = sysPrivilegeDto.getRemark();

        if(StringUtils.hasText(name) && !name.equals(exist.getName())) {
            name = name.trim();

            SysPrivilegeDto existOfName = findByName(name);
            ServiceAssert.assertThat(null != existOfName && existOfName.getId().longValue() != id.longValue(), "权限名已存在");
            exist.setName(name);
        }
        if(StringUtils.hasText(type) && !type.equals(exist.getType())) {
            ServiceAssert.assertThat(null == PrivilegeType.get(type), "无效权限类别");
            exist.setType(type);
        }
        if(null != sort && sort.intValue() != exist.getSort().intValue()) {
            Long pid = exist.getPid();
            List<SysPrivilegeDto> sysPrivilegeDtoList = findByPid(pid);
            if(!CollectionUtils.isEmpty(sysPrivilegeDtoList)) {
                for(SysPrivilegeDto dto : sysPrivilegeDtoList) {
                    ServiceAssert.assertThat(dto.getSort().intValue() == sort.intValue(), "相同父节点下已存在排序为" + sort + "的节点");
                }
            }
        }
        if(StringUtils.hasText(url) && !url.equals(exist.getUrl())) {
            exist.setUrl(url.trim());
        }
        if(StringUtils.hasText(remark) && !remark.equals(exist.getRemark())) {
            exist.setRemark(remark.trim());
        }

        sysPrivilegeDao.update(BeanUtils.copyProperties(exist, SysPrivilege.class));
        return sysPrivilegeDto;
    }

    @Override
    public List<SysPrivilegeDto> findAll() {
        List<SysPrivilege> sysPrivilegeList = sysPrivilegeDao.findAll();
        return BeanUtils.copyProperties(sysPrivilegeList, SysPrivilegeDto.class);
    }

}
