package com.studyolle.modules.account;

import com.studyolle.modules.event.Enrollment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true) // write를 안 써서 조금이라도 성능에 이점을 기대
public interface AccountRepository extends JpaRepository<Account, Long>, QuerydslPredicateExecutor<Account> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Account findByEmail(String email);

    Account findByNickname(String nickname);

    @EntityGraph(value = "Account.withTagsAndZones", type = EntityGraph.EntityGraphType.FETCH)
    Account findAccountWithTagsAndZonesById(Long id);
}
