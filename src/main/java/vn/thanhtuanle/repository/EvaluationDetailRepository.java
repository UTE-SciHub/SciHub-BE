package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.thanhtuanle.entity.EvaluationDetail;

public interface EvaluationDetailRepository extends JpaRepository<EvaluationDetail, Long> {

    @Query("""
        SELECT COUNT(ed) > 0
        FROM EvaluationDetail ed
        JOIN ed.councilMember cm
        JOIN cm.user u
        WHERE ed.evaluation.id = :topicApplicationId
        AND u.id = :evaluatorId
    """)
    boolean existsByEvaluationIdAndCouncilMemberUserId(
            @Param("topicApplicationId") Long topicApplicationId,
            @Param("evaluatorId") String evaluatorId
    );

}