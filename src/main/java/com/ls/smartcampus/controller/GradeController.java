package com.ls.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.smartcampus.pojo.Grade;
import com.ls.smartcampus.service.GradeService;
import com.ls.smartcampus.util.Result;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/gradeController")
@RequiredArgsConstructor
public class GradeController {

    @NonNull
    private GradeService gradeService;


    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @RequestBody List<Integer> ids
    ){
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @RequestBody Grade grade){
        // 接收参数
        // 调用服务层方法完成增加或修改
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }


    //sms/gradeController/getGrades/1/3?gradeName=%E4%B8%B9
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            String gradeName
    ){
        // 分页 带条件查询
        Page<Grade> page = new Page<>(pageNo,pageSize);
        // 通过服务层
        IPage<Grade> pageRes = gradeService.getGradeByOpr(page,gradeName);

        // 封装Result对象并返回
        return Result.ok(pageRes);
    }
}
