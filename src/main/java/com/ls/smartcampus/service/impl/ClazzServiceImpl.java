package com.ls.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.smartcampus.pojo.Clazz;
import com.ls.smartcampus.service.ClazzService;
import com.ls.smartcampus.mapper.ClazzMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* @author LS
* @description 针对表【tb_clazz】的数据库操作Service实现
* @createDate 2022-04-26 16:55:45
*/
@Service
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz>
    implements ClazzService{

    @Override
    public IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz) {

        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();

        String gradeName = clazz.getGradeName();
        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("grade_name",gradeName);
        }

        String name = clazz.getName();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByDesc("id");

        Page<Clazz> clazzPage = baseMapper.selectPage(page, queryWrapper);

        return clazzPage;
    }
}




