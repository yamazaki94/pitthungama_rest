package com.pitthungama.pitthungama_rest.services;

import com.pitthungama.pitthungama_rest.util.HtmlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class EmailSenderServiceImpl implements  EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    private HtmlGenerator htmlGenerator;
    private String fromEmail;
    private String fromName;

    @Autowired
    EmailSenderServiceImpl(HtmlGenerator htmlGenerator, MailProperties mailProperties){
        this.htmlGenerator = htmlGenerator;
        this.defaultEmailConfig(mailProperties);
    }

    private void defaultEmailConfig(MailProperties mailProperties){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setDefaultEncoding("UTF-8");

        this.fromEmail = mailSender.getUsername();
        this.fromName = mailSender.getUsername();

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailProperties.getProtocol());
        props.put("mail.smtp.auth", mailProperties.getProperties().getOrDefault("mail.smtp.auth","true"));
        props.put("mail.smtp.starttls.enable", mailProperties.getProperties().getOrDefault("mail.smtp.starttls.enable","true"));
        props.put("mail.debug",mailProperties.getProperties().getOrDefault("mail.debug","false"));

        this.emailSender = mailSender;
    }

    @Override
    public void sendHtmlMessage(String receiverEmail, String subject, Context context, String templateFileName) throws MessagingException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            String emailBody = htmlGenerator.generate(context, templateFileName);
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            //messageHelper.setTo(receiverEmail);
            messageHelper.setTo(new String[]{receiverEmail, "tarungupta1234@gmail.com", "tarun_apple@icloud.com"});
            messageHelper.setSubject(subject);
            messageHelper.setText(emailBody,true);
            messageHelper.setFrom(this.fromEmail, this.fromName);
        };
        emailSender.send(messagePreparator);

    }
}
