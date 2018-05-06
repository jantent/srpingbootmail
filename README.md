Spring boot中对邮件的发送也提供了支持，本篇文章只要是介绍sringboot中如何发送邮件。
如果对springboot很熟悉，那么这个熟悉起来也很快的。

## pom文件
```
  <properties>
        <java.version>1.8</java.version>
        <thymeleaf.version>3.0.0.RELEASE</thymeleaf.version>
        <thymeleaf-layout-dialect.version>2.0.0</thymeleaf-layout-dialect.version>
    </properties>
    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.1.RELEASE</version>
        <relativePath/>
    </parent>

    <dependencies>

        <!-- spring boot 配置 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- spring boot 热部署-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>


        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
    </dependencies>
```  
所需要的依赖全都在这里了。

### application.properties
这个配置文件需要配置一些，发送邮件所需要的配置
```
spring.mail.host=smtp.qq.com
spring.mail.port=587
spring.mail.username=xxxxxx
spring.mail.password=xxxxxx

spring.thymeleaf.cache=false

```  
推荐使用qq邮箱，163的会频繁报错，抽风。

### 发送邮件代码：

```
package com.service.impl;

import com.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author janti
 * @date 2018/5/3 22:07
 */
@Component
public class MailServiceImpl implements IMailService{

    @Autowired
    private JavaMailSender mailSender;


    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailFrom;

    /**
     * 发送简单邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendSimpleEmail(String to,String subject,String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * 发送html邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送带附件的邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param filepath
     */
    @Override
    public void sendFileMail(String to, String subject, String content, String filepath) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);

            FileSystemResource file = new FileSystemResource(new File(filepath));
            String fileName = filepath.substring(filepath.lastIndexOf(File.separator));
            helper.addAttachment(fileName,file);

            mailSender.send(mimeMessage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 使用模板来发送邮件
     *
     * @param to
     * @param subject
     */
    @Override
    public void sendTemplateMail(String to, String subject) {
        Context context = new Context();
        context.setVariable("username","jantent");
        String mailHtml =templateEngine.process("mail",context);
        sendHtmlMail(to,subject,mailHtml);
    }
}

```  
### 发送模板邮件
这里使用了thymeleaf作为渲染引擎，通常注册，验证，修改密码之类的可以发送模板邮件
```
<!DOCTYPE html>
<html lang="zh" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>Title</title>
</head>
<body>
<p>用户：</p>
<p th:text="${username}"></p>
<br>您好,这是我的github<br/>
<hr/>
<a  th:href="@{ https://github.com/JayTange }">链接地址</a>
</body>
</html>
```  

[代码在这里](https://github.com/JayTange/srpingbootmail.git)觉得有用的话 给个star