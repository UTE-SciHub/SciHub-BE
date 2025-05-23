package vn.thanhtuanle.service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import vn.thanhtuanle.model.dto.ReviewDTO;
import vn.thanhtuanle.model.request.ReviewRequest;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> findAll(Integer milestoneId);

    ReviewDTO findById(Integer id);

    @Transactional
    ReviewDTO create(ReviewRequest req) throws MessagingException;

    @Transactional
    ReviewDTO update(Integer id, ReviewRequest req);

    @Transactional
    void delete(Integer id);
}
