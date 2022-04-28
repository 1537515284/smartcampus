package com.ls.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.smartcampus.pojo.Admin;
import com.ls.smartcampus.service.AdminService;
import com.ls.smartcampus.util.MD5;
import com.ls.smartcampus.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
@RequiredArgsConstructor
public class AdminController {
    @NonNull
    private AdminService adminService;

    @ApiOperation("删除单个或多个管理员的信息")
    @DeleteMapping({"/deleteAdmin"})
    public Result deleteAdmin(@ApiParam("要删除的管理员id的JSON集合") @RequestBody List<Integer> ids) {
        this.adminService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("带条件的分页查询")
    @GetMapping({"/getAllAdmin/{pageNo}/{pageSize}"})
    public Result getAllAdmin(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件---模糊匹配的管理员名称") @RequestParam(value = "adminName", required = false) String name) {
        Page<Admin> page = new Page((long) pageNo, (long) pageSize);
        IPage<Admin> iPage = this.adminService.getAdminByOpr(page, name);
        return Result.ok(iPage);
    }

    @ApiOperation("保存或修改管理员信息")
    @PostMapping({"/saveOrUpdateAdmin"})
    public Result saveOrUpdateAdmin(@ApiParam("JSON格式的管理员信息") @RequestBody Admin admin) {
        Integer id = admin.getId();
        if (null == id || id == 0) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }

        this.adminService.saveOrUpdate(admin);
        return Result.ok();
    }
}
