package com.ls.smartcampus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.smartcampus.pojo.Grade;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LS
* @description 针对表【tb_grade】的数据库操作Service
* @createDate 2022-04-26 16:55:49
*/
public interface GradeService extends IService<Grade> {

    IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName);
}
