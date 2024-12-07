package com.example.memorygame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImageStorage {
    public static final String DEV_IMAGE_DIRECTORY = "src/main/resources/com/example/memorygame/user.home/custom-images";
    public static final String PROD_IMAGE_DIRECTORY = System.getProperty("user.home") + "/MemoryGame/custom-images";

    // ใช้ตัวแปรนี้เพื่อกำหนดโหมดปัจจุบัน (true = Development, false = Production)
    public static final boolean IS_DEVELOPMENT_MODE = true;

    public static String saveImage(File imageFile) throws IOException {
        // เลือกโฟลเดอร์ปลายทางตามโหมดการทำงาน
        String IMAGE_DIRECTORY = IS_DEVELOPMENT_MODE ? DEV_IMAGE_DIRECTORY : PROD_IMAGE_DIRECTORY;

        // ตรวจสอบนามสกุลไฟล์
        String fileName = imageFile.getName().toLowerCase();
        if (!(fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) {
            throw new IllegalArgumentException("File must be a valid image (.png, .jpg, .jpeg)");
        }

        // ตรวจสอบและสร้างโฟลเดอร์หากยังไม่มี
        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + IMAGE_DIRECTORY);
        }

        // จัดการกรณีไฟล์ชื่อซ้ำ
        String newImagePath = IMAGE_DIRECTORY + "/" + imageFile.getName();
        File newImageFile = new File(newImagePath);

        int counter = 1;
        while (newImageFile.exists()) {
            String baseName = imageFile.getName().substring(0, imageFile.getName().lastIndexOf('.'));
            String extension = imageFile.getName().substring(imageFile.getName().lastIndexOf('.'));
            newImagePath = IMAGE_DIRECTORY + "/" + baseName + "_" + counter + extension;
            newImageFile = new File(newImagePath);
            counter++;
        }

        // คัดลอกไฟล์ไปยังโฟลเดอร์ปลายทาง
        Files.copy(imageFile.toPath(), newImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Image saved successfully at: " + newImageFile.getAbsolutePath());
        return newImageFile.getAbsolutePath();
    }

    public static boolean deleteImage(String imagePath) {
        File file = new File(imagePath);
        return file.exists() && file.delete();
    }
}
