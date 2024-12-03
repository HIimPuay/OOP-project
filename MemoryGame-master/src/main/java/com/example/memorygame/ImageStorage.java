package com.example.memorygame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ImageStorage {

    // เมธอดสำหรับบันทึกภาพในโฟลเดอร์ที่กำหนด
    public static String saveImage(File sourceFile) throws IOException {
        // กำหนดโฟลเดอร์ปลายทาง (ควรเป็นโฟลเดอร์ในที่เขียนได้)
        String destinationDir = System.getProperty("user.home") + "/custom-images/";
        File destinationFolder = new File(destinationDir);

        // ตรวจสอบว่าโฟลเดอร์ปลายทางมีอยู่หรือไม่
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs(); // สร้างโฟลเดอร์หากยังไม่มี
        }

        // สร้างชื่อไฟล์แบบไม่ซ้ำ
        String uniqueFileName = System.currentTimeMillis() + "_" + sourceFile.getName();

        // กำหนดตำแหน่งไฟล์ปลายทาง
        Path destinationPath = new File(destinationFolder, uniqueFileName).toPath();

        // คัดลอกไฟล์ไปยังตำแหน่งปลายทาง
        Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // ส่งคืน path ของไฟล์ที่บันทึก
        return destinationPath.toString();
    }
}
