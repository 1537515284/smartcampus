package com.ls.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.smartcampus.pojo.Grade;
import com.ls.smartcampus.service.GradeService;
import com.ls.smartcampus.mapper.GradeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* @author LS
* @description 针对表【tb_grade】的数据库操作Service实现
* @createDate 2022-04-26 16:55:49
*/
@Service
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade>
    implements GradeService{

    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("name",gradeName);
        }
        queryWrapper.orderByDesc("id");

        Page<Grade> pageRes = baseMapper.selectPage(page, queryWrapper);

        return pageRes;
    }
}




