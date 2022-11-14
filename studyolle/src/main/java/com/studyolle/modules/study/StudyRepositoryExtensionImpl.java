package com.studyolle.modules.study;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.studyolle.modules.account.QAccount;
import com.studyolle.modules.tag.QTag;
import com.studyolle.modules.tag.Tag;
import com.studyolle.modules.zone.QZone;
import com.studyolle.modules.zone.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Set;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public Page<Study> findByKeyword(String keyword, Pageable pageable) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study).where(study.published.isTrue()
                .and(study.title.containsIgnoreCase(keyword))
                .or(study.tags.any().title.containsIgnoreCase(keyword))
                .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                .leftJoin(study.tags, QTag.tag).fetchJoin() // study의 tags는 QTag의 tag를 참조한 거임
                .leftJoin(study.zones, QZone.zone).fetchJoin()
                .distinct(); // distince를 해도 의미없음, 여전히 가져오는 데이터는 많음
        JPQLQuery<Study> pageableQuery = getQuerydsl().applyPagination(pageable, query); // QuerydslRepositorySupport에서 제공
        QueryResults<Study> fetchResults = pageableQuery.fetchResults();// 그냥 fetch는 데이터만 가져옴. 페이지 정보 포함해서 가져와야 함
        // contents 리스트, pageable, 전체 개수
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public List<Study> findByAccount(Set<Tag> tags, Set<Zone> zones) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study).where(study.published.isTrue()
                .and(study.closed.isFalse()) // 종료하지 않았고
                .and(study.tags.any().in(tags)) // account가 가지고 있는 tag 중 아무거나랑 매칭 되어야 함
                .and(study.zones.any().in(zones)))
                .leftJoin(study.tags, QTag.tag).fetchJoin()
                .leftJoin(study.zones, QZone.zone).fetchJoin()
                .orderBy(study.publishedDateTime.desc())
                .distinct()
                .limit(9);
        return query.fetch();
    }
}
