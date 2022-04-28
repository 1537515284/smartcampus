package com.ls.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.smartcampus.pojo.LoginForm;
import com.ls.smartcampus.pojo.Student;
import com.ls.smartcampus.service.StudentService;
import com.ls.smartcampus.mapper.StudentMapper;
import com.ls.smartcampus.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* @author LS
* @description 针对表【tb_student】的数据库操作Service实现
* @createDate 2022-04-26 16:55:56
*/
@Service
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long id) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> page, Student student) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        String name = student.getName();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }

        String clazzName = student.getClazzName();
        if (!StringUtils.isEmpty(clazzName)) {
            queryWrapper.like("clazz_name", clazzName);
        }
        queryWrapper.orderByDesc("id");
        Page<Student> studentPage = baseMapper.selectPage(page, queryWrapper);
        return studentPage;
    }
}




