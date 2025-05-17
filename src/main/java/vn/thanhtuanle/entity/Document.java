package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "document_type", length = 50, nullable = false)
    private String documentType;

    @Column(columnDefinition = "TEXT")
    private String filePath;

    private String publicId;

    private LocalDateTime uploadDate;

    private String originalFileName;
}