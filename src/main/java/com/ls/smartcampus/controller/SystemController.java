package com.ls.smartcampus.controller;

import com.ls.smartcampus.pojo.Admin;
import com.ls.smartcampus.pojo.LoginForm;
import com.ls.smartcampus.pojo.Student;
import com.ls.smartcampus.pojo.Teacher;
import com.ls.smartcampus.service.AdminService;
import com.ls.smartcampus.service.StudentService;
import com.ls.smartcampus.service.TeacherService;
import com.ls.smartcampus.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
@RequiredArgsConstructor
public class SystemController {

    @NonNull
    private final AdminService adminService;
    @NonNull
    private final StudentService studentService;
    @NonNull
    private final TeacherService teacherService;

    @ApiOperation("修改密码")
    @PostMapping({"updatePwd/{oldPassword}/{newPassword}"})
    public Result updatePwd(@RequestHeader("token") String token, @ApiParam("旧密码") @PathVariable String oldPassword, @ApiParam("新密码") @PathVariable String newPassword) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build((Object)null, ResultCodeEnum.TOKEN_ERROR);
        } else {
            // 获取用户id和类型
            Long userId = JwtHelper.getUserId(token);
            Integer userType = JwtHelper.getUserType(token);

            oldPassword = MD5.encrypt(oldPassword);
            newPassword = MD5.encrypt(newPassword);

            switch(userType) {
                case 1:
                    Admin admin = this.adminService.getAdminById(userId);
                    if (!oldPassword.equals(admin.getPassword())) {
                        return Result.fail().message("原密码有误!");
                    }

                    admin.setPassword(newPassword);
                    this.adminService.saveOrUpdate(admin);
                    break;
                case 2:
                    Student student = this.studentService.getStudentById(userId);
                    if (!oldPassword.equals(student.getPassword())) {
                        return Result.fail().message("原密码有误!");
                    }

                    student.setPassword(newPassword);
                    this.studentService.saveOrUpdate(student);
                    break;
                case 3:
                    Teacher teacher = this.teacherService.getTeacherById(userId);
                    if (!oldPassword.equals(teacher.getPassword())) {
                        return Result.fail().message("原密码有误!");
                    }

                    teacher.setPassword(newPassword);
                    this.teacherService.saveOrUpdate(teacher);
            }

            return Result.ok();
        }
    }

    @ApiOperation("文件上传统一入口")
    @PostMapping({"/headerImgUpload"})
    public Result headerImgUpload(
            @ApiParam("头像文件") @RequestPart("multipartFile") MultipartFile multipartFile
    ) {
        // 通过UUID生成新的文件名
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        String newFilename = uuid + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 获取 target目录下public/upload的绝对路径
        String parentPath = ClassUtils.getDefaultClassLoader().getResource("public/upload").getPath();
        String portraitPath = parentPath + "/" + newFilename;

        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        String path = "upload/" + newFilename;
        return Result.ok(path);
    }

    @ApiOperation("获取用户信息和类型")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        // 从token中解析出 用户id 和 用户的类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String ,Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",userType);
                map.put("user",admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType",userType);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",userType);
                map.put("user",teacher);
                break;
        }

        return Result.ok(map);
    }

    @ApiOperation("登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpSession session) {
        // 验证码校验
        String originalVerificationCode = (String) session.getAttribute("verificationCode");
        String receivedVerificationCode = loginForm.getVerifiCode();
        if ("".equals(originalVerificationCode) || null == originalVerificationCode) {
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if (!originalVerificationCode.equalsIgnoreCase(receivedVerificationCode)) {
            return Result.fail().message("验证码有误,请重新输入");
        }
        // 从session域中移除现有验证码
        session.removeAttribute("verificationCode");
        // 分用户类型进行校验

        // 准备一个map 用于存放响应的数据
        Map<String, Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        // 用户的类型和用户id转换成一个密文，以"token"的名称向客户端反馈
                        String token = JwtHelper.createToken(admin.getId().longValue(), loginForm.getUserType());
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        // 用户的类型和用户id转换成一个密文，以"token"的名称向客户端反馈
                        String token = JwtHelper.createToken(student.getId().longValue(), loginForm.getUserType());
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        // 用户的类型和用户id转换成一个密文，以"token"的名称向客户端反馈
                        String token = JwtHelper.createToken(teacher.getId().longValue(), loginForm.getUserType());
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户");
    }


    @ApiOperation("获取验证码图像")
    @GetMapping("/getVerifiCodeImage")
    public void getVerificationCodeImage(HttpServletRequest request, HttpServletResponse response){
        // 获取图片
        BufferedImage verificationCodeImage = CreateVerificationCodeImage.getVerificationCodeImage();
        // 获取图片上的验证码
        String verificationCode = new String(CreateVerificationCodeImage.getVerificationCode());
        // 将验证码文本放入session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verificationCode",verificationCode);

        // 将验证码响应给前端
        try {
            ImageIO.write(verificationCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
