package vn.thanhtuanle.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachedDocumentDTO {
    private MultipartFile file;
    private String description;
}
