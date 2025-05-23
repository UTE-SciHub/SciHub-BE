package vn.thanhtuanle.service;

import jakarta.transaction.Transactional;
import vn.thanhtuanle.model.dto.MilestoneDTO;
import vn.thanhtuanle.model.request.MilestoneRequest;

import java.util.List;

public interface MilestoneService {
    List<MilestoneDTO> findAll(String topicId);

    MilestoneDTO create(MilestoneRequest req);

    @Transactional
    MilestoneDTO update(Integer id, MilestoneRequest req);
}
