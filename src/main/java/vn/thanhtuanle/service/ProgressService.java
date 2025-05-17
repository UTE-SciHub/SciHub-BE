package vn.thanhtuanle.service;

import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.model.dto.ProgressDTO;
import vn.thanhtuanle.model.request.ProgressRequest;

import java.util.List;

public interface ProgressService {
    List<ProgressDTO> findAll(Integer milestoneId);
    ProgressDTO create(ProgressRequest req, MultipartFile file);
    ProgressDTO update(Integer id, ProgressRequest req);
    void delete(Integer id);
    ProgressDTO findById(Integer id);
}