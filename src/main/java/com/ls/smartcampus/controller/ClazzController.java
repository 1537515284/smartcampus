package com.ls.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.smartcampus.pojo.Clazz;
import com.ls.smartcampus.service.ClazzService;
import com.ls.smartcampus.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级控制器")
@RestController
@RequestMapping("/sms/clazzController")
@RequiredArgsConstructor
public class ClazzController {

    @NonNull
    private ClazzService clazzService;


    // /sms/clazzController/getClazzs
    @ApiOperation("获取所有班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> list = clazzService.list();
        return Result.ok(list);
    }

    // /sms/clazzController/deleteClazz     [1,2,3]
    @ApiOperation("删除单个或多个班级的信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @ApiParam("要删除的班级id的JSON集合") @RequestBody List<Integer> ids
    ){
        clazzService.removeByIds(ids);
        return Result.ok();
    }


    // /sms/clazzController/saveOrUpdateClazz
    @ApiOperation("保存或修改班级的信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("JSON格式的班级信息") @RequestBody Clazz clazz
    ){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }


    // /sms/clazzController/getClazzsByOpr/1/3?gradeName=二年级
    @ApiOperation("带条件的分页查询")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize")   Integer pageSize,
            @ApiParam("查询的条件") Clazz clazz
    ){
        Page<Clazz> page = new Page<>(pageNo,pageSize);

        IPage<Clazz> iPage = clazzService.getClazzByOpr(page,clazz);

        return Result.ok(iPage);
    }

}
