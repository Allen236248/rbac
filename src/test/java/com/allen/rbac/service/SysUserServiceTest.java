package com.allen.rbac.service;

import com.allen.rbac.dto.SysRoleDto;
import com.allen.rbac.dto.SysUserDto;
import com.allen.rbac.util.PageInfo;
import com.allen.rbac.util.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SysUserServiceTest {

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void testFindById() {
        SysUserDto sysUserDto = sysUserService.findById(23L);
        System.out.println(sysUserDto);
        List<SysRoleDto> roleList = sysUserDto.getRoleList();
        for(SysRoleDto sysRoleDto : roleList) {
            System.out.println(sysRoleDto);
        }
    }

    @Test
    public void testFindByUsername() {
        SysUserDto sysUserDto = sysUserService.findByUsername("test-a");
        System.out.println(sysUserDto);
        List<SysRoleDto> roleList = sysUserDto.getRoleList();
        for(SysRoleDto sysRoleDto : roleList) {
            System.out.println(sysRoleDto);
        }
    }

    @Test
    public void testFindByParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", "");
        params.put("roleId", "10");

        PageInfo pageInfo = PageInfo.build(2, 1);

        PageResult<SysUserDto> pageResult = sysUserService.findByParams(params, pageInfo);
        List<SysUserDto> sysUserDtoList = pageResult.getObjList();
        System.out.println(sysUserDtoList);
        for(SysUserDto sysUserDto : sysUserDtoList) {
            System.out.println(sysUserDto);
            List<SysRoleDto> roleList = sysUserDto.getRoleList();
            for(SysRoleDto sysRoleDto : roleList) {
                System.out.println(sysRoleDto);
            }
        }
    }
}
