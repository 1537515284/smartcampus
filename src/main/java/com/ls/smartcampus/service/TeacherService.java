package com.ls.smartcampus.service;

import com.ls.smartcampus.pojo.LoginForm;
import com.ls.smartcampus.pojo.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LS
* @description 针对表【tb_teacher】的数据库操作Service
* @createDate 2022-04-26 17:00:32
*/
public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);

    Teacher getTeacherById(Long id);
}
