package com.example.otech.util;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageStorageHelper {
    
    private static final String IMAGE_DIR = "product_images";
    
    /**
     * Copy image from assets or drawable to app internal storage
     * Returns file:// URI string
     */
    public static String copyImageToStorage(Context context, String imageName) {
        try {
            // Create images directory if not exists
            File imageDir = new File(context.getFilesDir(), IMAGE_DIR);
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            
            // Target file
            File targetFile = new File(imageDir, imageName);
            
            // If file already exists, return its URI
            if (targetFile.exists()) {
                return Uri.fromFile(targetFile).toString();
            }
            
            // Copy from drawable resources
            int resId = context.getResources().getIdentifier(
                imageName.replace(".jpg", "").replace(".png", ""), 
                "drawable", 
                context.getPackageName()
            );
            
            if (resId != 0) {
                InputStream in = context.getResources().openRawResource(resId);
                OutputStream out = new FileOutputStream(targetFile);
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                
                out.flush();
                out.close();
                in.close();
                
                return Uri.fromFile(targetFile).toString();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get file URI for existing image name
     */
    public static String getImageUri(Context context, String imageName) {
        File imageDir = new File(context.getFilesDir(), IMAGE_DIR);
        File imageFile = new File(imageDir, imageName);
        
        if (imageFile.exists()) {
            return Uri.fromFile(imageFile).toString();
        }
        
        return null;
    }
    
    /**
     * Initialize all product images from drawable to storage
     */
    public static void initializeImages(Context context) {
        // Laptop images
        for (int i = 1; i <= 5; i++) {
            copyImageToStorage(context, "laptop" + i + ".jpg");
        }
        
        // Banner images  
        for (int i = 1; i <= 6; i++) {
            copyImageToStorage(context, "banner" + i + ".jpg");
        }
    }
}
