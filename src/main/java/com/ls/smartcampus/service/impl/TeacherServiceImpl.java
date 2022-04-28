package com.ls.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.smartcampus.pojo.LoginForm;
import com.ls.smartcampus.pojo.Teacher;
import com.ls.smartcampus.service.TeacherService;
import com.ls.smartcampus.mapper.TeacherMapper;
import com.ls.smartcampus.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* @author LS
* @description 针对表【tb_teacher】的数据库操作Service实现
* @createDate 2022-04-26 17:00:32
*/
@Service
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService{

    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Teacher teacher = (Teacher)((TeacherMapper)this.baseMapper).selectOne(queryWrapper);
        return teacher;
    }

    public Teacher getTeacherById(Long id) {
        Teacher teacher = (Teacher)((TeacherMapper)this.baseMapper).selectById(id);
        return teacher;
    }

    public IPage<Teacher> getTeacherByOpr(Page<Teacher> page, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper();
        String name = teacher.getName();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }

        String clazzName = teacher.getClazzName();
        if (!StringUtils.isEmpty(clazzName)) {
            queryWrapper.like("clazz_name", clazzName);
        }

        queryWrapper.orderByDesc("id");
        Page<Teacher> teacherPage = (Page)((TeacherMapper)this.baseMapper).selectPage(page, queryWrapper);
        return teacherPage;
    }
}




