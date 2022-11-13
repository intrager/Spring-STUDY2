package com.studyolle.modules.study;

import com.querydsl.jpa.JPQLQuery;
import com.studyolle.modules.account.QAccount;
import com.studyolle.modules.tag.QTag;
import com.studyolle.modules.zone.QZone;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public List<Study> findByKeyword(String keyword) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study).where(study.published.isTrue()
                .and(study.title.containsIgnoreCase(keyword))
                .or(study.tags.any().title.containsIgnoreCase(keyword))
                .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                .leftJoin(study.tags, QTag.tag).fetchJoin() // study의 tags는 QTag의 tag를 참조한 거임
                .leftJoin(study.zones, QZone.zone).fetchJoin()
                .leftJoin(study.members, QAccount.account).fetchJoin()
                .distinct(); // distince를 해도 의미없음, 여전히 가져오는 데이터는 많음
        return query.fetch();
    }
}