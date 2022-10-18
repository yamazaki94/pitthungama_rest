package com.pitthungama.pitthungama_rest.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pitthungama.pitthungama_rest.services.QRCodeService;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/barcodes")
public class BarcodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${application.amazons3bucket.name}")
    private String bucketName;

    private String destPath = System.getProperty("java.io.tmpdir");

    private FileOutputStream fileOutputStream;


    @GetMapping(value = "/generateQRCode/{barcode}")
    public ResponseEntity<?> generateQRCode (@PathVariable String barcode) throws IOException {

        byte[] qrCode = qrCodeService.generateQRCode(barcode, 500, 500);

        String base64EncodedImageBytes = Base64.getEncoder().encodeToString(qrCode);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(base64EncodedImageBytes);
    }


    @GetMapping(value = "/generateQRCode/{barcode}/upload")
    public ResponseEntity<?> uploadQRCode (@PathVariable String barcode) throws IOException {
        try {
            String fileName = barcode + ".jpeg";

            byte[] qrCode = qrCodeService.generateQRCode(barcode, 500, 500);

            InputStream inputStream = new ByteArrayInputStream(qrCode);

            MultipartFile file = new MockMultipartFile(fileName ,fileName, ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);

        } catch (AmazonServiceException serviceException) {
            throw serviceException;
        } catch (AmazonClientException clientException) {
            throw clientException;
        }

        return new ResponseEntity<>("File Uploaded", OK);
    }
}
