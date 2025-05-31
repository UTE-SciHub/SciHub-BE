package vn.thanhtuanle.service.impl;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.service.MailService;
import vn.thanhtuanle.entity.Council;
import vn.thanhtuanle.entity.Milestone;
import vn.thanhtuanle.entity.Review;
import vn.thanhtuanle.entity.TopicMember;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.ReviewDTO;
import vn.thanhtuanle.model.request.ReviewRequest;
import vn.thanhtuanle.model.response.EmailRecipient;
import vn.thanhtuanle.repository.CouncilRepository;
import vn.thanhtuanle.repository.MilestoneRepository;
import vn.thanhtuanle.repository.ReviewRepository;
import vn.thanhtuanle.service.ReviewService;
import vn.thanhtuanle.service.TopicService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CouncilRepository councilRepository;
    private final MilestoneRepository milestoneRepository;
    private final TopicService topicService;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private static final String MAIL_TEMPLATE_PATH = "/template/review-notification.html";
    private static final String VIEW_DETAIL_REVIEW_URL = "http://localhost:5173/my-topics";

    @Override
    public List<ReviewDTO> findAll(Integer milestoneId) {
        log.info("Fetching all reviews for milestone ID: {}", milestoneId);

        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", String.valueOf(milestoneId)));

        List<Review> reviews = reviewRepository.findByMilestoneAndDelFlagFalse(milestone);

        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO findById(Integer id) {
        log.info("Fetching review with id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", String.valueOf(id)));
        return modelMapper.map(review, ReviewDTO.class);
    }

    @Transactional
    @Override
    public ReviewDTO create(ReviewRequest req) throws MessagingException {
        log.info("Creating new review for council: {} and milestone: {}", req.getCouncilId(), req.getMilestoneId());

        Council council = councilRepository.findById(req.getCouncilId())
                .orElseThrow(() -> new ResourceNotFoundException("Council", "id", req.getCouncilId()));

        Milestone milestone = milestoneRepository.findById(req.getMilestoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", String.valueOf(req.getMilestoneId())));

        Review review = Review.builder()
                .council(council)
                .milestone(milestone)
                .comments(req.getComments())
                .delFlag(false)
                .build();

        Review savedReview = reviewRepository.save(review);
        log.info("Review created with ID: {}", savedReview.getId());

        List<TopicMember> members = topicService.getMembersOfTopic(milestone.getTopic().getId());
        List<EmailRecipient> recipients = members.stream()
                .map(member -> new EmailRecipient(
                        Optional.ofNullable(member.getUser().getName()).filter(name -> !name.isBlank()).orElse("Thành viên"),
                        member.getUser().getEmail()
                ))
                .toList();

        String topicName = Optional.ofNullable(milestone.getTopic().getTopicCode())
                .filter(code -> !code.isBlank())
                .orElse(milestone.getTopic().getVietnameseName());

        String milestoneDescription = milestone.getDescription();
        String reviewDate = savedReview.getCreatedAt() != null
                ? formatDateTime(savedReview.getCreatedAt())
                : formatDateTime(LocalDateTime.now());

        mailService.sendFeedbackNotificationEmail(
                recipients,
                topicName,
                council.getName(),
                review.getComments(),
                milestoneDescription,
                reviewDate,
                MAIL_TEMPLATE_PATH,
                VIEW_DETAIL_REVIEW_URL
        );

        return modelMapper.map(savedReview, ReviewDTO.class);
    }

    @Transactional
    @Override
    public ReviewDTO update(Integer id, ReviewRequest req) {
        log.info("Updating review with id: {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", String.valueOf(id)));

        if (!review.getCouncil().getId().equals(req.getCouncilId())) {
            Council newCouncil = councilRepository.findById(req.getCouncilId())
                    .orElseThrow(() -> new ResourceNotFoundException("Council", "id", req.getCouncilId()));
            review.setCouncil(newCouncil);
        }

        if (!review.getMilestone().getId().equals(req.getMilestoneId())) {
            Milestone newMilestone = milestoneRepository.findById(req.getMilestoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", String.valueOf(req.getMilestoneId())));
            review.setMilestone(newMilestone);
        }

        review.setComments(req.getComments());

        Review updatedReview = reviewRepository.save(review);
        log.info("Review updated successfully");

        return modelMapper.map(updatedReview, ReviewDTO.class);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        log.info("Deleting review with id: {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", String.valueOf(id)));

        reviewRepository.delete(review);

        log.info("Review soft deleted successfully");
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a"));
    }
}