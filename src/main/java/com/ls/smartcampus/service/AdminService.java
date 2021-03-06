package com.ls.smartcampus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.smartcampus.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ls.smartcampus.pojo.LoginForm;

/**
* @author LS
* @description 针对表【tb_admin】的数据库操作Service
* @createDate 2022-04-26 16:55:14
*/
public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long id);

    IPage<Admin> getAdminByOpr(Page<Admin> page, String name);
}
