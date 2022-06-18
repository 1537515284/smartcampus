package com.ls.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.smartcampus.pojo.Admin;
import com.ls.smartcampus.pojo.LoginForm;
import com.ls.smartcampus.service.AdminService;
import com.ls.smartcampus.mapper.AdminMapper;
import com.ls.smartcampus.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* @author LS
* @description 针对表【tb_admin】的数据库操作Service实现
* @createDate 2022-04-26 16:55:14
*/
@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

    public Admin login(LoginForm loginForm) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    public Admin getAdminById(Long id) {
        return baseMapper.selectById(id);
    }

    public IPage<Admin> getAdminByOpr(Page<Admin> page, String name) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }

        queryWrapper.orderByDesc("id");
        Page<Admin> adminPage = baseMapper.selectPage(page, queryWrapper);
        return adminPage;
    }
}




