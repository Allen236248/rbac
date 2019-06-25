package com.allen.rbac.controller;

import com.allen.rbac.dto.SysRoleDto;
import com.allen.rbac.dto.req.AddRoleRequestDto;
import com.allen.rbac.dto.req.ListRoleRequestDto;
import com.allen.rbac.dto.req.UpdateRoleRequestDto;
import com.allen.rbac.service.SysRoleService;
import com.allen.rbac.util.ApiResult;
import com.allen.rbac.util.PageInfo;
import com.allen.rbac.util.PageResult;
import com.allen.rbac.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("sys_role")
public class SysRoleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysRoleController.class);

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/add")
    public ApiResult<SysRoleDto> addSysRole(@RequestBody AddRoleRequestDto addRoleRequestDto) {
        ApiResult<SysRoleDto> apiResult = ApiResult.build();
        try {
            SysRoleDto sysRoleDto = new SysRoleDto();
            sysRoleDto.setName(addRoleRequestDto.getName());
            sysRoleDto.setCode(addRoleRequestDto.getCode());
            sysRoleDto.setPrivilegeIdList(addRoleRequestDto.getPrivilegeIdList());
            sysRoleService.addSysRole(sysRoleDto);

            apiResult.setAttach(sysRoleDto);
        } catch (ServiceException se) {
            LOGGER.error("增加角色失败，" + se.getMessage());
            apiResult.error("增加角色失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("增加角色失败，" + e.getMessage());
            apiResult.error("增加角色失败，" + e.getMessage());
        }
        return apiResult;
    }

    @PostMapping("/list")
    public ApiResult<PageResult<SysRoleDto>> listSysRole(@RequestBody ListRoleRequestDto listRoleRequestDto) {
        ApiResult<PageResult<SysRoleDto>> apiResult = ApiResult.build();
        try {
            Map<String, Object> params = new HashMap<>();
            String name = listRoleRequestDto.getName();
            if(StringUtils.hasText(name)) {
                params.put("name", name.trim());
            }

            PageInfo pageInfo = buildPageInfo(listRoleRequestDto);

            PageResult<SysRoleDto> result = sysRoleService.findByParams(params, pageInfo);
            apiResult.setAttach(result);
        } catch (ServiceException se) {
            LOGGER.error("查询角色列表失败，" + se.getMessage());
            apiResult.error("查询角色列表失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("查询角色列表失败，" + e.getMessage());
            apiResult.error("查询角色列表失败，" + e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/get")
    public ApiResult<SysRoleDto> getSysRole(@RequestParam Long id) {
        ApiResult<SysRoleDto> apiResult = ApiResult.build();
        try {
            SysRoleDto sysRoleDto = sysRoleService.findById(id);
            apiResult.setAttach(sysRoleDto);
        } catch (ServiceException se) {
            LOGGER.error("查询角色失败，" + se.getMessage());
            apiResult.error("查询角色失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("查询角色失败，" + e.getMessage());
            apiResult.error("查询角色失败，" + e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/del")
    public ApiResult<Void> delSysRole(@RequestParam Long id) {
        ApiResult<Void> apiResult = ApiResult.build();
        try {
            sysRoleService.deleteById(id);
        } catch (ServiceException se) {
            LOGGER.error("删除角色失败，" + se.getMessage());
            apiResult.error("删除角色失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("删除角色失败，" + e.getMessage());
            apiResult.error("删除角色失败，" + e.getMessage());
        }
        return apiResult;
    }

    @PostMapping("/update")
    public ApiResult<SysRoleDto> updateSysRole(@RequestBody UpdateRoleRequestDto updateRoleRequestDto) {
        ApiResult<SysRoleDto> apiResult = ApiResult.build();
        try {
            SysRoleDto sysRoleDto = new SysRoleDto();
            sysRoleDto.setId(updateRoleRequestDto.getId());
            sysRoleDto.setName(updateRoleRequestDto.getName());
            sysRoleDto.setPrivilegeIdList(updateRoleRequestDto.getPrivilegeIdList());
            sysRoleService.updateSysRole(sysRoleDto);

            apiResult.setAttach(sysRoleDto);
        } catch (ServiceException se) {
            LOGGER.error("修改角色失败，" + se.getMessage());
            apiResult.error("修改角色失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改角色失败，" + e.getMessage());
            apiResult.error("修改角色失败，" + e.getMessage());
        }
        return apiResult;
    }

}
