package com.kutub.ecommerce.ecommerce_api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    public String uploadImage(String path, MultipartFile file) throws IOException {
        // ফাইলের আসল নাম (যেমন: abc.jpg)
        String name = file.getOriginalFilename();

        // রেন্ডম নাম তৈরি করা যাতে দুইটা ফাইলের নাম এক না হয়ে যায় (যেমন: d3f2-abc.jpg)
        String randomID = UUID.randomUUID().toString();
        String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));

        // ফুল পাথ তৈরি করা
        String filePath = path + File.separator + fileName1;

        // ফোল্ডার তৈরি করা যদি না থাকে
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        // ফাইল কপি করা
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName1;
    }
}
