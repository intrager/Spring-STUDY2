package com.studyolle.modules.study;

import com.studyolle.modules.account.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryExtension {
    boolean existsByPath(String path);

    @EntityGraph(value = "Study.withAll", type = EntityGraph.EntityGraphType.LOAD) // 연관된 애들은 일단 EAGER, 그 외 애들은 걔네 지정값대로
    Study findByPath(String path); // 이걸 사용하면 study, member, tag, zone, manager 데이터 같이 조인
    // 요청이 많이 들어오는 상황에서는 차라리 EAGER로 한 번에 무겁게 가져오는 게 나음. 즉, 쿼리 개수를 줄이는 방향으로 감

    @EntityGraph(value = "Study.withTagsAndManagers", type = EntityGraph.EntityGraphType.FETCH)
    Study findStudyWithTagsByPath(String path);

    @EntityGraph(value = "Study.withZonesAndManagers", type = EntityGraph.EntityGraphType.FETCH)
    Study findStudyWithZonesByPath(String path);

    @EntityGraph(value = "Study.withManagers", type = EntityGraph.EntityGraphType.FETCH)
    Study findStudyWithManagersByPath(String path);

    @EntityGraph(value = "Study.withMembers", type = EntityGraph.EntityGraphType.FETCH)
    Study findStudyWithMembersByPath(String path);

    Study findStudyOnlyByPath(String path);

    // Study.withTagsAndZones이 이름에 해당하는 엔티티 그래프를 자주 사용할 때 쓰는 방법
    @EntityGraph(value = "Study.withTagsAndZones", type = EntityGraph.EntityGraphType.FETCH)
    Study findStudyWithTagsAndZonesById(Long id);

    @EntityGraph(value = "Study.withManagersAndMembers", type = EntityGraph.EntityGraphType.FETCH)
    Study findStudyWithManagersAndMembersById(Long id);

    List<Study> findFirst9ByPublishedAndClosedOrderByPublishedDateTimeDesc(boolean published, boolean closed);

    List<Study> findFirst5ByManagersContainingAndClosedOrderByPublishedDateTimeDesc(Account account, boolean closed);

    List<Study> findFirst5ByMembersContainingAndClosedOrderByPublishedDateTimeDesc(Account account, boolean closed);
}

/**
 *     @EntityGraph(attributePaths = {"tags", "zones", "managers", "members"}, type = EntityGraph.EntityGraphType.LOAD) // 연관된 애들은 일단 EAGER, 그 외 애들은 걔네 지정값대로
 *     Study findByPath(String path); // 이걸 사용하면 study, member, tag, zone, manager 데이터 같이 조인
 *     // 요청이 많이 들어오는 상황에서는 차라리 EAGER로 한 번에 무겁게 가져오는 게 나음. 즉, 쿼리 개수를 줄이는 방향으로 감
 *
 *     @EntityGraph(attributePaths = {"tags", "managers"})
 *     Study findStudyWithTagsByPath(String path);
 *
 *     @EntityGraph(attributePaths = {"zones", "managers"})
 *     Study findStudyWithZonesByPath(String path);
 *
 *     @EntityGraph(attributePaths = "managers")
 *     Study findStudyWithManagersByPath(String path);
 *
 *     @EntityGraph(attributePaths = "members")
 *     Study findStudyWithMembersByPath(String path);
 *
 *     Study findStudyOnlyByPath(String path);
 *
 *     // Study.withTagsAndZones이 이름에 해당하는 엔티티 그래프를 자주 사용할 때 쓰는 방법
 *     @EntityGraph(attributePaths = {"zones", "tags"})
 *     Study findStudyWithTagsAndZonesById(Long id);
 *
 *     @EntityGraph(attributePaths = {"members", "managers"}) // 근데 이렇게 해도 됨
 *     Study findStudyWithManagersAndMembersById(Long id);

 */