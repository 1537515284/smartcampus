package com.ls.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.smartcampus.pojo.Teacher;
import com.ls.smartcampus.service.TeacherService;
import com.ls.smartcampus.util.MD5;
import com.ls.smartcampus.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"教师控制器"})
@RestController
@RequestMapping({"/sms/teacherController"})
@RequiredArgsConstructor
public class TeacherController {

    @NonNull
    private TeacherService teacherService;

    @ApiOperation("删除单条或多条教师信息")
    @DeleteMapping({"/deleteTeacher"})
    public Result deleteTeacher(@ApiParam("要删除的教师信息的id集合") @RequestBody List<Integer> ids) {
        this.teacherService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("保存或修改教师的信息")
    @PostMapping({"/saveOrUpdateTeacher"})
    public Result saveOrUpdateTeacher(
            @ApiParam("JSON格式的教师信息") @RequestBody Teacher teacher
    ) {
        Integer id = teacher.getId();
        if (null == id || id == 0) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }

        this.teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @ApiOperation("带条件的分页查询")
    @GetMapping({"/getTeachers/{pageNo}/{pageSize}"})
    public Result getTeachers(
            @ApiParam("分页查询的页码数") @PathVariable Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable Integer pageSize,
            @ApiParam("查询条件---模糊匹配的教师信息") Teacher teacher) {
        Page<Teacher> page = new Page((long)pageNo, (long)pageSize);
        IPage<Teacher> iPage = this.teacherService.getTeacherByOpr(page, teacher);
        return Result.ok(iPage);
    }
}