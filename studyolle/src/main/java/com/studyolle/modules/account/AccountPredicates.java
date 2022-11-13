package com.studyolle.modules.account;

import com.querydsl.core.types.Predicate;
import com.studyolle.modules.tag.Tag;
import com.studyolle.modules.zone.Zone;

import java.util.Set;

public class AccountPredicates {
    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        QAccount account = QAccount.account;
        // account가 가지고 있는 zones 그리고 tags 내에서 하나라도 매핑이 되는지 리턴 (범주 내에서 아무거나 하나라도 가지고 있으면 됨)
        return account.zones.any().in(zones).and(account.tags.any().in(tags));
    }
}
