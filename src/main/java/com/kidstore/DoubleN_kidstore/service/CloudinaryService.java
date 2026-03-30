package com.kidstore.DoubleN_kidstore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dusilduzn"); // Copy trên trang chủ Cloudinary
        config.put("api_key", "942977532452175");
        config.put("api_secret", "V0Bbom-zvQQUfisqxXI_9sXgi5M");
        cloudinary = new Cloudinary(config);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString(); // Trả về cái link ảnh đã up lên mây
    }
}