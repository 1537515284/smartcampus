package com.ls.smartcampus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.smartcampus.pojo.Teacher;
import com.ls.smartcampus.service.TeacherService;
import com.ls.smartcampus.mapper.TeacherMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author LS
* @description 针对表【tb_teacher】的数据库操作Service实现
* @createDate 2022-04-26 17:00:32
*/
@Service
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService{

}




