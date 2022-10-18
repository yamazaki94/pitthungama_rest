package com.pitthungama.pitthungama_rest.services;

public interface QRCodeService {
    byte[] generateQRCode(String qrContent, int width, int height);

    void printQRCode(String qrContent, int width, int height);

}
