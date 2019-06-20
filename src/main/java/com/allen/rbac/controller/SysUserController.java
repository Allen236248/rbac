package com.allen.rbac.controller;

import com.allen.rbac.dto.SysUserDto;
import com.allen.rbac.dto.req.AddUserRequestDto;
import com.allen.rbac.dto.req.ListUserRequestDto;
import com.allen.rbac.dto.req.UpdateUserRequestDto;
import com.allen.rbac.service.SysUserService;
import com.allen.rbac.util.ApiResult;
import com.allen.rbac.util.PageInfo;
import com.allen.rbac.util.PageResult;
import com.allen.rbac.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("sys_user")
public class SysUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/add")
    public ApiResult<SysUserDto> addSysUser(@RequestBody AddUserRequestDto addUserRequestDto) {
        ApiResult<SysUserDto> apiResult = ApiResult.build();
        try {
            SysUserDto sysUserDto = new SysUserDto();
            sysUserDto.setUsername(addUserRequestDto.getUsername());
            sysUserDto.setMobile(addUserRequestDto.getMobile());
            sysUserDto.setRoleIdList(addUserRequestDto.getRoleIdList());
            sysUserService.addSysUser(sysUserDto);

            apiResult.setAttach(sysUserDto);
        } catch (ServiceException se) {
            LOGGER.error("增加用户失败，" + se.getMessage());
            apiResult.error("增加用户失败，" + se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("增加用户失败，" + e.getMessage());
            apiResult.error("增加用户失败，" + e.getMessage());
        }
        return apiResult;
    }

    @PostMapping("/list")
    public ApiResult<PageResult<SysUserDto>> listSysUser(@RequestBody ListUserRequestDto listUserRequestDto) {
        ApiResult<PageResult<SysUserDto>> apiResult = ApiResult.build();
        try {
            Map<String, Object> params = new HashMap<>();
            String mobile = listUserRequestDto.getMobile();
            if(StringUtils.hasText(mobile)) {
                params.put("mobile", mobile.trim());
            }

            Long roleId = listUserRequestDto.getRoleId();
            if(null != roleId && roleId.longValue() > 0) {
                params.put("roleId", roleId);
            }

            PageInfo pageInfo = buildPageInfo(listUserRequestDto);

            PageResult<SysUserDto> result = sysUserService.findByParams(params, pageInfo);
            apiResult.setAttach(result);
        } catch (ServiceException se) {
            LOGGER.error("查询用户列表失败，" + se.getMessage());
            apiResult.error("查询用户列表失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("查询用户列表失败，" + e.getMessage());
            apiResult.error("查询用户列表失败，" + e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/get")
    public ApiResult<SysUserDto> getSysUser(@RequestParam Long id) {
        ApiResult<SysUserDto> apiResult = ApiResult.build();
        try {
            SysUserDto sysUserDto = sysUserService.findById(id);
            apiResult.setAttach(sysUserDto);
        } catch (ServiceException se) {
            LOGGER.error("查询用户失败，" + se.getMessage());
            apiResult.error("查询用户失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("查询用户失败，" + e.getMessage());
            apiResult.error("查询用户失败，" + e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/del")
    public ApiResult<Void> delSysUser(@RequestParam Long id) {
        ApiResult<Void> apiResult = ApiResult.build();
        try {
            sysUserService.deleteById(id);
        } catch (ServiceException se) {
            LOGGER.error("删除用户失败，" + se.getMessage());
            apiResult.error("删除用户失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("删除用户失败，" + e.getMessage());
            apiResult.error("删除用户失败，" + e.getMessage());
        }
        return apiResult;
    }

    @PostMapping("/update")
    public ApiResult<SysUserDto> updateSysUser(@RequestBody UpdateUserRequestDto updateUserRequestDto) {
        ApiResult<SysUserDto> apiResult = ApiResult.build();
        try {
            SysUserDto sysUserDto = new SysUserDto();
            sysUserDto.setUsername(updateUserRequestDto.getUsername());
            sysUserDto.setMobile(updateUserRequestDto.getMobile());
            sysUserDto.setPassword(updateUserRequestDto.getPassword());
            sysUserDto.setRoleIdList(updateUserRequestDto.getRoleIdList());
            sysUserService.updateSysUser(sysUserDto);

            apiResult.setAttach(sysUserDto);
        } catch (ServiceException se) {
            LOGGER.error("修改用户失败，" + se.getMessage());
            apiResult.error("修改用户失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改用户失败，" + e.getMessage());
            apiResult.error("修改用户失败，" + e.getMessage());
        }
        return apiResult;
    }

}
