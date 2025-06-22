package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import vn.thanhtuanle.entity.Council;

import java.util.List;
import java.util.Map;

public interface CouncilRepository extends JpaRepository<Council, Long>, JpaSpecificationExecutor<Council> {

    boolean existsByName(String name);

    boolean existsByDecisionNumber(String decisionNumber);

    boolean existsByIdAndDecisionNumber(Long id, String decisionNumber);

    boolean existsByIdAndName(Long id, String name);

    @Query("SELECT COUNT(c) FROM Council c WHERE c.delFlag = false")
    long countTotalCouncils();

    @Query("SELECT COUNT(c) FROM Council c WHERE c.startDate <= CURRENT_DATE AND c.endDate >= CURRENT_DATE AND c.delFlag = false")
    long countActiveCouncils();

    @Query("SELECT COUNT(c) FROM Council c WHERE c.startDate > CURRENT_DATE AND c.delFlag = false")
    long countUpcomingCouncils();

    @Query("SELECT COUNT(c) FROM Council c WHERE c.endDate < CURRENT_DATE AND c.delFlag = false")
    long countClosedCouncils();

    @Query("SELECT new map(c.id as id, c.name as name, " +
            "COUNT(tc.topic) as topicCount, c.startDate as startDate, c.endDate as endDate) " +
            "FROM Council c LEFT JOIN c.topicCouncils tc " +
            "WHERE c.startDate <= CURRENT_DATE AND c.endDate >= CURRENT_DATE AND c.delFlag = false " +
            "GROUP BY c.id, c.name, c.startDate, c.endDate")
    List<Map<String, Object>> findActiveCouncils();
}
