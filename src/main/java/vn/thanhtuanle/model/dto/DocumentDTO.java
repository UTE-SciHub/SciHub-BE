package vn.thanhtuanle.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDTO {
    private Integer id;
    private String documentType;
    private String filePath;
    private String publicId;
    private LocalDateTime uploadDate;
    private String originalFileName;
}
