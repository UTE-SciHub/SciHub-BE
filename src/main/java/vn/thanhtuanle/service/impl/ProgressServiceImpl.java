package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.service.CloudinaryService;
import vn.thanhtuanle.entity.Milestone;
import vn.thanhtuanle.entity.Progress;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.ProgressDTO;
import vn.thanhtuanle.model.request.ProgressRequest;
import vn.thanhtuanle.repository.MilestoneRepository;
import vn.thanhtuanle.repository.ProgressRepository;
import vn.thanhtuanle.service.ProgressService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final MilestoneRepository milestoneRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<ProgressDTO> findAll(Integer milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", String.valueOf(milestoneId)));

        List<Progress> progressList = progressRepository.findByMilestone(milestone);

        return progressList.stream()
                .map(progress -> modelMapper.map(progress, ProgressDTO.class))
                .toList();
    }

    @Override
    public ProgressDTO findById(Integer id) {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress", "id", String.valueOf(id)));

        return modelMapper.map(progress, ProgressDTO.class);
    }

    @Override
    @Transactional
    public ProgressDTO create(ProgressRequest req, MultipartFile file) {
        Integer milestoneId = req.getMilestoneId();

        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", String.valueOf(milestoneId)));

        // Validate progress percent
        if (req.getProgressPercent() < 0 || req.getProgressPercent() > 100) {
            throw new IllegalArgumentException("Progress percentage must be between 0 and 100");
        }

        String documentUrl = null;
        if (file != null && !file.isEmpty()) {
            try {
                Map result = cloudinaryService.upload(file);
                documentUrl = String.valueOf(result.get("url"));
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload file", e);
            }
        }

        Progress progress = Progress.builder()
                .milestone(milestone)
                .progressPercent(req.getProgressPercent())
                .report(req.getReport())
                .documentUrl(documentUrl)
                .build();

        Progress savedProgress = progressRepository.save(progress);

        return modelMapper.map(savedProgress, ProgressDTO.class);
    }

    @Override
    @Transactional
    public ProgressDTO update(Integer id, ProgressRequest req) {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress", "id", String.valueOf(id)));

        // If milestone is being changed, verify it exists
        if (!progress.getMilestone().getId().equals(req.getMilestoneId())) {
            Milestone newMilestone = milestoneRepository.findById(req.getMilestoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", String.valueOf(req.getMilestoneId())));
            progress.setMilestone(newMilestone);
        }

        // Validate progress percent
        if (req.getProgressPercent() < 0 || req.getProgressPercent() > 100) {
            throw new IllegalArgumentException("Progress percentage must be between 0 and 100");
        }

        progress.setProgressPercent(req.getProgressPercent());
        progress.setReport(req.getReport());
        progress.setDocumentUrl(req.getDocumentUrl());

        Progress updatedProgress = progressRepository.save(progress);

        return modelMapper.map(updatedProgress, ProgressDTO.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress", "id", String.valueOf(id)));

        progressRepository.delete(progress);
    }
}