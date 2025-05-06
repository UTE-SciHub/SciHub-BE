package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.thanhtuanle.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
