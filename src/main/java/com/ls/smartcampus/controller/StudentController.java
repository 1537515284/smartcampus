package com.ls.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.smartcampus.pojo.Student;
import com.ls.smartcampus.service.StudentService;
import com.ls.smartcampus.util.MD5;
import com.ls.smartcampus.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生管理器")
@RestController
@RequestMapping("/sms/studentController")
@RequiredArgsConstructor
public class StudentController {

    @NonNull
    private StudentService studentService;


    // /sms/studentController/delStudentById
    @ApiOperation("删除单个或多个学生的信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @ApiParam("要删除的学生id的JSON集合") @RequestBody List<Integer> ids
            ){
        studentService.removeByIds(ids);
        return Result.ok();
    }

    // /sms/studentController/addOrUpdateStudent
    @ApiOperation("新增或修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdate(
            @ApiParam("JSON格式的学生信息") @RequestBody Student student
    ){
        Integer id = student.getId();
        if(null == id || 0 == id){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }


    // /sms/studentController/getStudentByOpr/1/3?name=李四&clazzName=一年一班
    @ApiOperation("分页带条件查询")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件---模糊匹配的学生姓名") Student student
    ){
        Page<Student> page = new Page<>(pageNo,pageSize);
        IPage<Student> iPage = studentService.getStudentByOpr(page,student);
        return Result.ok(iPage);
    }


}
