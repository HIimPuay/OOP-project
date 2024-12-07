package com.example.memorygame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageStorage {
    // Define the path to the "custom-images" folder inside the project directory
    public static final String CUSTOM_IMAGE_FOLDER;

    static {
        // Set the path relative to the project's resources directory (custom-images
        // folder)
        CUSTOM_IMAGE_FOLDER = Paths.get(
                System.getProperty("user.dir"),
                "MemoryGame-master/src/main/resources/com/example/memorygame/custom-images").toString(); // Save
                                                                                                         // directly in
                                                                                                         // the project
        // directory
    }

    // Function to save the card image inside the "custom-images" folder
    public static String saveCardImage(File imageFile) throws IOException {
        // Check if the file is a valid image (PNG, JPG, JPEG)
        String fileName = imageFile.getName().toLowerCase();
        if (!(fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) {
            throw new IllegalArgumentException("File must be a valid image (.png, .jpg, .jpeg)");
        }

        // Create the directory if it doesn't exist
        File directory = new File(CUSTOM_IMAGE_FOLDER);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + CUSTOM_IMAGE_FOLDER);
            }
        }

        // Generate the new file path for saving the image
        String newCardPath = CUSTOM_IMAGE_FOLDER + "/" + imageFile.getName();
        File newCardFile = new File(newCardPath);

        // If the file already exists, add a counter to the filename to avoid
        // overwriting
        int counter = 1;
        while (newCardFile.exists()) {
            String baseName = imageFile.getName().substring(0, imageFile.getName().lastIndexOf('.'));
            String extension = imageFile.getName().substring(imageFile.getName().lastIndexOf('.'));
            newCardPath = CUSTOM_IMAGE_FOLDER + "/" + baseName + "_" + counter + extension;
            newCardFile = new File(newCardPath);
            counter++;
        }

        // Copy the file to the new location
        Files.copy(imageFile.toPath(), newCardFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Card saved successfully at: " + newCardFile.getAbsolutePath());
        return newCardFile.getAbsolutePath();
    }

    // Function to delete the card from the folder
    public static boolean deleteCard(String cardPath) {
        File file = new File(cardPath);
        return file.exists() && file.delete();
    }
}
