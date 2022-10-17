package com.pitthungama.pitthungama_rest.controller;

import com.pitthungama.pitthungama_rest.services.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

@RestController
@RequestMapping("/barcodes")
public class BarcodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping(value = "/generateQRCode/{barcode}")
    public ResponseEntity<?> generateQRCode (@PathVariable String barcode) throws IOException {

        byte[] qrCode = qrCodeService.generateQRCode(barcode, 500, 500);

        String base64EncodedImageBytes = Base64.getEncoder().encodeToString(qrCode);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(base64EncodedImageBytes);
    }
}
