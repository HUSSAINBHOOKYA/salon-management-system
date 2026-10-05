package com.salonmanagementsystem.util;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Component
public class FileUploadUtil {

    private final Cloudinary cloudinary;

    public FileUploadUtil(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file, String folderName) throws IOException {

        if (file == null || file.isEmpty()) {
            return null;
        }

        Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "salonhub/" + folderName,
                        "resource_type", "image"
                )
        );

        return (String) result.get("secure_url");
    }
}









//package com.salonmanagementsystem.util;
//
//import java.io.IOException;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.UUID;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//@Component
//public class FileUploadUtil {
//
//    private static final String UPLOAD_DIR = "uploads";
//
//    public String uploadFile(MultipartFile file, String folderName) throws IOException {
//
//        if (file == null || file.isEmpty()) {
//            return null;
//        }
//
//        Path uploadPath = Paths.get(UPLOAD_DIR, folderName);
//
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        String originalFileName = file.getOriginalFilename();
//
//        String extension = "";
//
//        if (originalFileName != null && originalFileName.contains(".")) {
//            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
//        }
//
//        String fileName = UUID.randomUUID() + extension;
//
//        Path filePath = uploadPath.resolve(fileName);
//
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//        return "/uploads/" + folderName + "/" + fileName;
//    }
//
//}