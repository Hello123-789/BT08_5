package vn.iotstar.services.impl;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.services.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation = Paths.get("uploads");

    public StorageServiceImpl() {
        try {
            Files.createDirectories(rootLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStorageFilename(MultipartFile file, String prefix) {
        String ext = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return (prefix != null ? prefix + "_" : "") + UUID.randomUUID().toString().substring(0, 8) + ext;
    }

    @Override
    public String store(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            Path targetDir = rootLocation.resolve(subDir != null ? subDir : "");
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            String filename = getStorageFilename(file, null);
            Path destinationFile = targetDir.resolve(Paths.get(filename)).normalize().toAbsolutePath();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return filename;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
