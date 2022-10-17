package com.pitthungama.pitthungama_rest.controller;

import com.pitthungama.pitthungama_rest.model.EmailRequestBody;
import com.pitthungama.pitthungama_rest.services.EmailSenderService;
import com.pitthungama.pitthungama_rest.services.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired EmailSenderService emailService;

    @PostMapping(value = "/sendEmail")
    public ResponseEntity<?> generateQRCode (@RequestBody EmailRequestBody requestBody) throws IOException, MessagingException {

        byte[] qrCode = qrCodeService.generateQRCode(requestBody.ticketCode, 500, 500);

        String base64EncodedImageBytes = Base64.getEncoder().encodeToString(qrCode);

        Context emailBodyData = new Context();
        Map<String, Object> variable = new HashMap<String, Object>();
        variable.put("baseImage", base64EncodedImageBytes);
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



