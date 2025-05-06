package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.thanhtuanle.entity.CouncilMember;

public interface CouncilMemberRepository extends JpaRepository<CouncilMember, Long> {

    @Query("SELECT cm FROM CouncilMember cm WHERE cm.user.email = :userId AND cm.council.id = :councilId")
    CouncilMember findByUserIdAndCouncilId(String userId, Long councilId);
}
