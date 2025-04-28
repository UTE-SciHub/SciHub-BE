package vn.thanhtuanle.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachedDocumentDTO {
    private String filePath;
    private String description;
    private String publicId;
}
