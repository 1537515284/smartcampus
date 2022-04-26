package com.ls.smartcampus.service;

import com.ls.smartcampus.pojo.LoginForm;
import com.ls.smartcampus.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LS
* @description 针对表【tb_student】的数据库操作Service
* @createDate 2022-04-26 16:55:56
*/
public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);
}
