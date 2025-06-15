package vn.thanhtuanle.common.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@Slf4j
public class CloudinaryService {
    private Cloudinary cloudinary;

    @Value("${application.cloudinary.cloud-name}")
    private String cloudName;

    @Value("${application.cloudinary.api-key}")
    private String apiKey;

    @Value("${application.cloudinary.api-secret}")
    private String apiSecret;

    private static final String[] ALLOWED_IMAGE_TYPES = {"jpg", "jpeg", "png", "gif"};
    private static final String[] FILE_TYPES = {"pdf", "docx", "doc", "pptx", "ppt", "xlsx", "xls"};
    private static final String BASE_FOLDER = "UTE-SciHub";
    private static final String IMAGE_FOLDER = BASE_FOLDER + "/images";
    private static final String PDF_FOLDER = BASE_FOLDER + "/pdf";

    @PostConstruct
    public void init() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", cloudName);
        valuesMap.put("api_key", apiKey);
        valuesMap.put("api_secret", apiSecret);
        this.cloudinary = new Cloudinary(valuesMap);
    }

    public Map upload(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        String fileExtension = getFileExtension(multipartFile.getOriginalFilename());
        if (!isSupportedFileType(fileExtension)) {
            throw new IllegalArgumentException("Unsupported file type: " + fileExtension);
        }

        File tempFile = convert(multipartFile);
        try {
            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("resource_type", isImage(fileExtension) ? "image" : "raw");
            uploadParams.put("folder", isImage(fileExtension) ? IMAGE_FOLDER : PDF_FOLDER);

            return cloudinary.uploader().upload(tempFile, uploadParams);
        } finally {
            try {
                Files.deleteIfExists(tempFile.toPath());
            } catch (IOException e) {
                System.err.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
            }
        }
    }

    public List<Map<String, Object>> uploadAll(List<MultipartFile> multipartFiles) {
        List<Map<String, Object>> results = new ArrayList<>();
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            log.warn("No files provided for uploadAll");
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "No files provided");
            results.add(errorResult);
            return results;
        }

        for (MultipartFile file : multipartFiles) {
            Map<String, Object> result = new HashMap<>();
            result.put("originalFilename", file.getOriginalFilename());
            try {
                validateFile(file);
                String fileExtension = getFileExtension(file.getOriginalFilename());
                validateFileType(fileExtension);

                File tempFile = convert(file);
                try {
                    Map<String, Object> uploadParams = new HashMap<>();
                    uploadParams.put("resource_type", isImage(fileExtension) ? "image" : "raw");
                    uploadParams.put("folder", isImage(fileExtension) ? IMAGE_FOLDER : PDF_FOLDER);

                    log.info("Uploading file {} in batch", file.getOriginalFilename());
                    Map uploadResult = cloudinary.uploader().upload(tempFile, uploadParams);
                    result.put("status", "success");
                    result.put("uploadResult", uploadResult);
                } finally {
                    deleteTempFile(tempFile);
                }
            } catch (Exception e) {
                log.error("Failed to upload file {}: {}", file.getOriginalFilename(), e.getMessage());
                result.put("status", "failed");
                result.put("error", e.getMessage());
            }
            results.add(result);
        }
        return results;
    }

    public Map delete(String id, String resourceType) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("resource_type", resourceType);
        return cloudinary.uploader().destroy(id, params);
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        String tempFileName = UUID.randomUUID() + "_" +
                Objects.requireNonNull(multipartFile.getOriginalFilename());
        File tempFile = File.createTempFile("cloudinary_", tempFileName);

        try (FileOutputStream fo = new FileOutputStream(tempFile)) {
            fo.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isSupportedFileType(String extension) {
        return isImage(extension) || isDocument(extension);
    }

    private boolean isImage(String extension) {
        for (String imageType : ALLOWED_IMAGE_TYPES) {
            if (imageType.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDocument(String extension) {
        for (String docType : FILE_TYPES) {
            if (docType.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }
    }

    private void validateFileType(String extension) {
        if (!isSupportedFileType(extension)) {
            throw new IllegalArgumentException("Unsupported file type: " + extension);
        }
    }

    private void deleteTempFile(File tempFile) {
        try {
            Files.deleteIfExists(tempFile.toPath());
            log.debug("Deleted temporary file: {}", tempFile.getAbsolutePath());
        } catch (IOException e) {
            log.warn("Failed to delete temporary file: {}", tempFile.getAbsolutePath(), e);
        }
    }
}
