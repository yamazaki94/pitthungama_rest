package com.pitthungama.pitthungama_rest.services;

import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.validation.constraints.Email;


public interface EmailSenderService {

    void sendHtmlMessage(String receiverEmail, String subject, Context context, String templateFileName) throws MessagingException;
}
