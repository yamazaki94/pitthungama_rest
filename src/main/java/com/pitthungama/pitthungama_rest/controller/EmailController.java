package com.pitthungama.pitthungama_rest.controller;

import com.pitthungama.pitthungama_rest.model.EmailRequestBody;
import com.pitthungama.pitthungama_rest.services.EmailSenderService;
import com.pitthungama.pitthungama_rest.services.QRCodeService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired EmailSenderService emailService;

    private org.slf4j.Logger LOGGER = LoggerFactory.getLogger(getClass());

    @PostMapping(value = "/sendEmail")
    public ResponseEntity<?> generateQRCode (@RequestBody EmailRequestBody requestBody) throws IOException, MessagingException {

        Context emailBodyData = new Context();
        Map<String, Object> variable = new HashMap<String, Object>();
        String imageUrl = "https://pitthungamabucket.s3.ap-southeast-1.amazonaws.com/"+ requestBody.ticketCode + ".jpeg";
        variable.put("imageUrl", imageUrl);
        variable.put("receiverName", requestBody.receiverName);
        variable.put("participants", requestBody.participants);
        variable.put("payment", requestBody.payment);

        emailBodyData.setVariable("context", variable);

        emailService.sendHtmlMessage(requestBody.receiverEmail ,
                "Test Email",
                emailBodyData,
                "welcome-email");

        return new ResponseEntity<>("Email Sent", OK);
    }
}



