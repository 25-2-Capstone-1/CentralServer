package com.centralserver.demo.domain.googlemap;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
public class GoogleMapImageDownloader {

    public byte[] download(String url) throws IOException {
        URL imageUrl = new URL(url);

        try (InputStream is = imageUrl.openStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[2048];
            int length;

            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }

            return baos.toByteArray();
        }
    }
}