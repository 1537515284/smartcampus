package com.ls.smartcampus.controller;

import com.ls.smartcampus.pojo.Admin;
import com.ls.smartcampus.pojo.LoginForm;
import com.ls.smartcampus.pojo.Student;
import com.ls.smartcampus.pojo.Teacher;
import com.ls.smartcampus.service.AdminService;
import com.ls.smartcampus.service.StudentService;
import com.ls.smartcampus.service.TeacherService;
import com.ls.smartcampus.util.CreateVerificationCodeImage;
import com.ls.smartcampus.util.JwtHelper;
import com.ls.smartcampus.util.Result;
import com.ls.smartcampus.util.ResultCodeEnum;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

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
