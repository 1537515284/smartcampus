package com.ls.smartcampus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.smartcampus.pojo.Clazz;
import com.ls.smartcampus.service.ClazzService;
import com.ls.smartcampus.mapper.ClazzMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author LS
* @description 针对表【tb_clazz】的数据库操作Service实现
* @createDate 2022-04-26 16:55:45
*/
@Service
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz>
    implements ClazzService{

}




