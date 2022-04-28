package com.ls.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.smartcampus.pojo.Grade;
import com.ls.smartcampus.service.GradeService;
import com.ls.smartcampus.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
@RequiredArgsConstructor
public class GradeController {

    @NonNull
    private GradeService gradeService;

    @ApiOperation("获取所有年级信息")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> list = gradeService.list();
        return Result.ok(list);
    }


    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("要删除的所有的grade的id的JSON集合") @RequestBody List<Integer> ids
    ){
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("新增或修改grade，有id属性是修改，没有是增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("JSON格式的Grade对象") @RequestBody Grade grade){
        // 接收参数
        // 调用服务层方法完成增加或修改
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }


    //sms/gradeController/getGrades/1/3?gradeName=%E4%B8%B9
    @ApiOperation("根据年级名称模糊查询，带分页功能")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询的页码") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("模糊匹配的年级名称") String gradeName
    ){
        // 分页 带条件查询
        Page<Grade> page = new Page<>(pageNo,pageSize);
        // 通过服务层
        IPage<Grade> pageRes = gradeService.getGradeByOpr(page,gradeName);

        // 封装Result对象并返回
        return Result.ok(pageRes);
    }
}
