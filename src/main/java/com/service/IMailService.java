package com.service;

/**
 * @author tangj
 * @date 2018/5/3 22:06
 */
public interface IMailService {
     void sendSimpleEmail(String to,String subject,String content);

     void sendHtmlMail(String to,String subject,String content);

     void sendFileMail(String to,String subject,String content,String filepath);

     void sendTemplateMail(String to,String subject);
}
