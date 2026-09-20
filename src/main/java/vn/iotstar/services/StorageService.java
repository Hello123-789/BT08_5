package vn.iotstar.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String subDir);
    String getStorageFilename(MultipartFile file, String prefix);
}
