package com.allen.rbac.service.impl;

import com.allen.rbac.dao.SysUserDao;
import com.allen.rbac.dto.SysUserDto;
import com.allen.rbac.entity.SysUser;
import com.allen.rbac.enums.UserStatus;
import com.allen.rbac.service.SysUserRoleService;
import com.allen.rbac.service.SysUserService;
import com.allen.rbac.util.BeanUtils;
import com.allen.rbac.util.PageInfo;
import com.allen.rbac.util.PageResult;
import com.allen.rbac.util.ServiceAssert;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Transactional
    @Override
    public SysUserDto addSysUser(SysUserDto sysUserDto) {
        String username = sysUserDto.getUsername();
        ServiceAssert.assertThat(!StringUtils.hasText(username), "用户名不能为空");

        String mobile = sysUserDto.getMobile();
        ServiceAssert.assertThat(!StringUtils.hasText(mobile), "手机号码不能为空");

        //去除左右空格
        username = username.trim();
        ServiceAssert.assertThat(null != findByUsername(username), "用户名已存在");

        //去除左右空格
        mobile = mobile.trim();
        ServiceAssert.assertThat(null != findByMobile(mobile), "手机号码已存在");

        sysUserDto.setUsername(username);
        sysUserDto.setMobile(mobile);
        //生成默认密码。默认为123456；盐值使用username
        sysUserDto.setPassword(new Md5Hash("123456", username, 1).toHex());
        sysUserDto.setStatus(UserStatus.NORMAL.getId());
        SysUser sysUser = BeanUtils.copyProperties(sysUserDto, SysUser.class);
        sysUserDao.insert(sysUser);

        Long userId = sysUser.getId();
        List<Long> roleIdList = sysUserDto.getRoleIdList();
        sysUserRoleService.saveBatch(userId, roleIdList);

        sysUserDto.setId(userId);
        return sysUserDto;
    }

    @Override
    public SysUserDto findById(Long id) {
        ServiceAssert.assertThat(null == id || id.longValue() < 1, "用户ID不能为空或小于1");

        SysUser sysUser = sysUserDao.findById(id);
        return BeanUtils.copyProperties(sysUser, SysUserDto.class);
    }

    @Override
    public SysUserDto findByUsername(String username) {
        ServiceAssert.assertThat(!StringUtils.hasText(username), "用户名不能为空");

        SysUser sysUser = sysUserDao.findByUsername(username);
        return BeanUtils.copyProperties(sysUser, SysUserDto.class);
    }

    @Override
    public SysUserDto findByMobile(String mobile) {
        ServiceAssert.assertThat(!StringUtils.hasText(mobile), "手机号码不能为空");

        SysUser sysUser = sysUserDao.findByMobile(mobile);
        return BeanUtils.copyProperties(sysUser, SysUserDto.class);
    }

    @Override
    public PageResult<SysUserDto> findByParams(Map<String, Object> params, PageInfo pageInfo) {
        ServiceAssert.assertThat(null == pageInfo, "分页信息不能为空");

        params.put("page", pageInfo);
        List<SysUser> sysUserList = sysUserDao.findByParams(params);
        return PageResult.build(pageInfo, BeanUtils.copyProperties(sysUserList, SysUserDto.class));
    }

    @Override
    public void deleteById(Long id) {
        ServiceAssert.assertThat(null == id && id.longValue() < 1, "用户ID不能为空或小于1");

        sysUserDao.updateStatus(id, 3);
    }

    @Transactional
    @Override
    public SysUserDto updateSysUser(SysUserDto sysUserDto) {
        Long id = sysUserDto.getId();
        ServiceAssert.assertThat(null == id || id.longValue() < 1, "用户ID不能为空或小于1");

        SysUserDto exist = findById(id);
        ServiceAssert.assertThat(null == exist, "用户不存在");

        String username = sysUserDto.getUsername();
        if(StringUtils.hasText(username) && !username.equals(exist.getUsername())) {
            username = username.trim();

            SysUserDto existOfUsername = findByUsername(username);
            ServiceAssert.assertThat(null != existOfUsername && existOfUsername.getId().longValue() != id.longValue(), "用户名已存在");
            exist.setUsername(username);
        }

        String mobile = sysUserDto.getMobile();
        if(StringUtils.hasText(username) && !mobile.equals(exist.getMobile())) {
            mobile = mobile.trim();

            SysUserDto existOfMobile = findByMobile(mobile);
            ServiceAssert.assertThat(null != existOfMobile && existOfMobile.getId().longValue() != id.longValue(), "手机号码已存在");
            exist.setMobile(mobile);
        }

        String password = sysUserDto.getPassword();
        if(StringUtils.hasText(password)) {
            //修改密码。盐值使用username
            username = exist.getUsername();
            exist.setPassword(new Md5Hash(password, username, 1).toHex());
        }

        sysUserDao.update(BeanUtils.copyProperties(exist, SysUser.class));

        List<Long> roleIdList = sysUserDto.getRoleIdList();
        sysUserRoleService.saveBatch(id, roleIdList);

        return sysUserDto;
    }

}
