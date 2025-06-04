package com.ssafy.mvc.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadUtil {

	@Value("${file.upload.board.path}")
    private String uploadDir;

    @Value("${file.access.url.prefix}")
    private String urlPrefix;

    public String saveFile(MultipartFile file) throws IOException {
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path savePath = new File(uploadPath, fileName).toPath();
        Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

        return urlPrefix.endsWith("/") ? urlPrefix + fileName : urlPrefix + "/" + fileName;
    }

    public String getDefaultImagePath() {
        return urlPrefix + "ssafit_default.png";
    }
}
