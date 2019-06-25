package com.allen.rbac.shiro;

import com.allen.rbac.dto.SysPrivilegeDto;
import com.allen.rbac.dto.SysRoleDto;
import com.allen.rbac.dto.SysUserDto;
import com.allen.rbac.service.SysRoleService;
import com.allen.rbac.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomShiroRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomShiroRealm.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 认证（登陆）:用来进行身份认证，即验证用户输入的用户名和密码是否正确
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        LOGGER.info("进行身份认证");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();//token.getPrincipal()
        String password = String.valueOf(token.getPassword());//token.getCredentials()

        // 通过username从数据库查找用户信息。
        // 实际项目中，可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔限制的，2分钟内不会重复执行该方法
        SysUserDto sysUserDto = sysUserService.findByUsername(username);
        LOGGER.info("进行身份认证-用户信息：" + sysUserDto);
        if(null == sysUserDto) {
            LOGGER.info("进行身份认证-失败，根据用户名{}查询的用户信息为空", username);
            throw new AuthenticationException("用户名或密码错误");
        }
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        return new SimpleAuthenticationInfo(sysUserDto, sysUserDto.getPassword(), credentialsSalt, getName());
    }

    /**
     * 授权
     *
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        LOGGER.info("请求授权");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUserDto sysUserDto = (SysUserDto) principal.getPrimaryPrincipal();
        if(null == sysUserDto) {
            LOGGER.warn("请求授权-用户信息为空");
            return null;
        }
        List<SysRoleDto> roleList = sysUserDto.getRoleList();
        if(null == roleList || roleList.isEmpty()) {
            LOGGER.warn("请求授权-用户的角色为空");
            return null;
        }
        List<Long> roleIdList = new ArrayList<>();
        for(SysRoleDto role : roleList) {
            roleIdList.add(role.getId());
        }

        List<SysRoleDto> sysRoleDtoList = sysRoleService.findByIdList(roleIdList);
        for(SysRoleDto sysRoleDto : sysRoleDtoList) {
            List<SysPrivilegeDto> sysPrivilegeDtoList = sysRoleDto.getPrivilegeList();
            if(null == sysPrivilegeDtoList || sysPrivilegeDtoList.isEmpty()) {
                LOGGER.warn("请求授权-用户的角色：" + sysRoleDto.getName() + " 对应的权限为空");
                continue;
            }

            authorizationInfo.addRole(sysRoleDto.getName());
            for(SysPrivilegeDto sysPrivilegeDto : sysPrivilegeDtoList) {
                //
                authorizationInfo.addStringPermission(sysPrivilegeDto.getCode());
            }
        }
        return authorizationInfo;
    }

}
