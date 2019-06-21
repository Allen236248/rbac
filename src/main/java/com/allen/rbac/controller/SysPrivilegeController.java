package com.allen.rbac.controller;

import com.allen.rbac.dto.SysPrivilegeDto;
import com.allen.rbac.dto.req.AddPrivilegeRequestDto;
import com.allen.rbac.dto.req.UpdatePrivilegeRequestDto;
import com.allen.rbac.service.SysPrivilegeService;
import com.allen.rbac.util.ApiResult;
import com.allen.rbac.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sys_privilege")
public class SysPrivilegeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysPrivilegeController.class);

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    @PostMapping("/add")
    public ApiResult<SysPrivilegeDto> addSysPrivilege(@RequestBody AddPrivilegeRequestDto addPrivilegeRequestDto) {
        ApiResult<SysPrivilegeDto> apiResult = ApiResult.build();
        try {
            SysPrivilegeDto sysPrivilegeDto = new SysPrivilegeDto();
            sysPrivilegeDto.setPid(addPrivilegeRequestDto.getPid());
            sysPrivilegeDto.setPlatform(addPrivilegeRequestDto.getPlatform());
            sysPrivilegeDto.setName(addPrivilegeRequestDto.getName());
            sysPrivilegeDto.setType(addPrivilegeRequestDto.getType());
            sysPrivilegeDto.setUrl(addPrivilegeRequestDto.getUrl());
            sysPrivilegeDto.setSort(addPrivilegeRequestDto.getSort());
            sysPrivilegeDto.setRemark(addPrivilegeRequestDto.getRemark());
            sysPrivilegeService.addSysPrivilege(sysPrivilegeDto);

            apiResult.setAttach(sysPrivilegeDto);
        } catch (ServiceException se) {
            LOGGER.error("增加权限失败，" + se.getMessage());
            apiResult.error("增加权限失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("增加权限失败，" + e.getMessage());
            apiResult.error("增加权限失败，" + e.getMessage());
        }
        return apiResult;
    }

    @PostMapping("/list")
    public ApiResult<List<SysPrivilegeDto>> listSysPrivilege() {
        ApiResult<List<SysPrivilegeDto>> apiResult = ApiResult.build();
        try {
            List<SysPrivilegeDto> sysPrivilegeDtoList = sysPrivilegeService.findAll();
            apiResult.setAttach(sysPrivilegeDtoList);
        } catch (ServiceException se) {
            LOGGER.error("查询权限列表失败，" + se.getMessage());
            apiResult.error("查询权限列表失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("查询权限列表失败，" + e.getMessage());
            apiResult.error("查询权限列表失败，" + e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/get")
    public ApiResult<SysPrivilegeDto> getSysPrivilege(@RequestParam Long id) {
        ApiResult<SysPrivilegeDto> apiResult = ApiResult.build();
        try {
            SysPrivilegeDto sysPrivilegeDto = sysPrivilegeService.findById(id);
            apiResult.setAttach(sysPrivilegeDto);
        } catch (ServiceException se) {
            LOGGER.error("查询权限失败，" + se.getMessage());
            apiResult.error("查询权限失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("查询权限失败，" + e.getMessage());
            apiResult.error("查询权限失败，" + e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/del")
    public ApiResult<Void> delSysPrivilege(@RequestParam Long id) {
        ApiResult<Void> apiResult = ApiResult.build();
        try {
            sysPrivilegeService.disableSysPrivilege(id);
        } catch (ServiceException se) {
            LOGGER.error("删除权限失败，" + se.getMessage());
            apiResult.error("删除权限失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("删除权限失败，" + e.getMessage());
            apiResult.error("删除权限失败，" + e.getMessage());
        }
        return apiResult;
    }

    @PostMapping("/update")
    public ApiResult<SysPrivilegeDto> updateSysPrivilege(@RequestBody UpdatePrivilegeRequestDto updatePrivilegeRequestDto) {
        ApiResult<SysPrivilegeDto> apiResult = ApiResult.build();
        try {
            SysPrivilegeDto sysPrivilegeDto = new SysPrivilegeDto();
            sysPrivilegeDto.setId(updatePrivilegeRequestDto.getId());
            sysPrivilegeDto.setName(updatePrivilegeRequestDto.getName());
            sysPrivilegeDto.setType(updatePrivilegeRequestDto.getType());
            sysPrivilegeDto.setUrl(updatePrivilegeRequestDto.getUrl());
            sysPrivilegeDto.setSort(updatePrivilegeRequestDto.getSort());
            sysPrivilegeDto.setRemark(updatePrivilegeRequestDto.getRemark());
            sysPrivilegeService.updateSysPrivilege(sysPrivilegeDto);

            apiResult.setAttach(sysPrivilegeDto);
        } catch (ServiceException se) {
            LOGGER.error("修改权限失败，" + se.getMessage());
            apiResult.error("修改权限失败，" + se.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改权限失败，" + e.getMessage());
            apiResult.error("修改权限失败，" + e.getMessage());
        }
        return apiResult;
    }

}
