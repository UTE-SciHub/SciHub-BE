package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.thanhtuanle.entity.Council;
import vn.thanhtuanle.entity.CouncilMember;

import java.util.List;

public interface CouncilMemberRepository extends JpaRepository<CouncilMember, Long> {

    @Query("SELECT cm FROM CouncilMember cm WHERE cm.user.email = :userId AND cm.council.id = :councilId")
    CouncilMember findByUserIdAndCouncilId(String userId, Long councilId);

    List<CouncilMember> findByCouncil(Council council);
}
