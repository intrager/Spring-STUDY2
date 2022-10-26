package com.studyolle.study;

import com.studyolle.domain.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long> {
    boolean existsByPath(String path);

    @EntityGraph(value = "Study.withAll", type = EntityGraph.EntityGraphType.LOAD) // 연관된 애들은 일단 EAGER, 그 외 애들은 걔네 지정값대로
    Study findByPath(String path); // 이걸 사용하면 study, member, tag, zone, manager 데이터 같이 조인
    // 요청이 많이 들어오는 상황에서는 차라리 EAGER로 한 번에 무겁게 가져오는 게 나음. 즉, 쿼리 개수를 줄이는 방향으로 감
}
