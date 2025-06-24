package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.entity.Milestone;
import vn.thanhtuanle.entity.Progress;
import vn.thanhtuanle.entity.Review;
import vn.thanhtuanle.entity.Topic;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.MilestoneDTO;
import vn.thanhtuanle.model.dto.ProgressDTO;
import vn.thanhtuanle.model.request.MilestoneRequest;
import vn.thanhtuanle.repository.MilestoneRepository;
import vn.thanhtuanle.repository.ProgressRepository;
import vn.thanhtuanle.repository.TopicRepository;
import vn.thanhtuanle.service.MilestoneService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MilestoneServiceImpl implements MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;
    private final ProgressRepository progressRepository;

    @Override
    public List<MilestoneDTO> findAll(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        List<Milestone> milestones = milestoneRepository.findByTopicAndDelFlagFalse(topic);

        return milestones.stream()
                .map(milestone -> {
                    MilestoneDTO milestoneDTO = modelMapper.map(milestone, MilestoneDTO.class);
                    List<Progress> progressList = progressRepository.findByMilestone(milestone);
                    List<ProgressDTO> progressDTOs = progressList.stream()
                            .map(progress -> modelMapper.map(progress, ProgressDTO.class))
                            .toList();
                    milestoneDTO.setProgressReports(progressDTOs);

                    return milestoneDTO;
                })
                .toList();
    }

    @Override
    @Transactional
    public MilestoneDTO create(MilestoneRequest req) {
        String topicId = req.getTopicId();

        if (!topicRepository.existsById(topicId)) {
            throw new ResourceNotFoundException("Topic", "id", topicId);
        }

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        LocalDate currentDate = LocalDate.now();
        if (req.getExpectedCompletionDate().isBefore(currentDate)) {
            throw new IllegalArgumentException("expectedCompletionDate must be in the future");
        }

        Milestone milestone = Milestone.builder()
                .topic(topic)
                .description(req.getDescription())
                .expectedCompletionDate(req.getExpectedCompletionDate())
                .status(req.getStatus())
                .delFlag(false)
                .build();

        Milestone savedMilestone = milestoneRepository.save(milestone);

        return modelMapper.map(savedMilestone, MilestoneDTO.class);
    }

    @Transactional
    @Override
    public MilestoneDTO update(Integer id, MilestoneRequest req) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", String.valueOf(id)));

        // If topic is being changed, verify it exists
        if (!milestone.getTopic().getId().equals(req.getTopicId())) {
            Topic newTopic = topicRepository.findById(req.getTopicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", req.getTopicId()));
            milestone.setTopic(newTopic);
        }

        milestone.setDescription(req.getDescription());
        milestone.setExpectedCompletionDate(req.getExpectedCompletionDate());
        milestone.setStatus(req.getStatus());

        Milestone updatedMilestone = milestoneRepository.save(milestone);

        return modelMapper.map(updatedMilestone, MilestoneDTO.class);
    }
}
