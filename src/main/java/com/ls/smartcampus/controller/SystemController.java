package com.ls.smartcampus.controller;

import com.ls.smartcampus.util.CreateVerificationCodeImage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/sms/system")
public class SystemController {


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
