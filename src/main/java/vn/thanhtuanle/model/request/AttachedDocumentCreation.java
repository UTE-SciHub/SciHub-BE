package vn.thanhtuanle.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachedDocumentCreation {
    private String id;
    private MultipartFile file;
    private String description;
    private String url;
}
