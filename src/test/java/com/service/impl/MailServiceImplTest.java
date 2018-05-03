package com.service.impl;

import com.service.IMailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author tangj
 * @date 2018/5/3 22:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceImplTest {

    @Resource
    IMailService mailService;
    String to = "jantent@qq.com";
    String subject= "主题aaa";
    String context = "你好，这是个测试";

    @Test
    public void testSendSimpleMail(){
        mailService.sendSimpleEmail(to,subject,context);
    }

    @Test
    public void testHtmlMail(){
        String htmlcontent="<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封Html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMail(to,subject,htmlcontent);
    }

    @Test
    public void testFileMail(){
        String filePath = "D:\\测试邮件.docx";
        mailService.sendFileMail(to,subject,context,filePath);
    }

    @Test
    public  void testTemPlate(){
        mailService.sendTemplateMail(to,subject);
    }
}