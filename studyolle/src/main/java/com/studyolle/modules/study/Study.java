package com.studyolle.modules.study;

import com.studyolle.modules.account.Account;
import com.studyolle.modules.account.UserAccount;
import com.studyolle.modules.zone.Zone;
import com.studyolle.modules.tag.Tag;
import lombok.*;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// @EntityGraph에다가 attributePaths 만 놓으면 밑에 어노테이션들 지울 수 있음
@NamedEntityGraph(name = "Study.withAll", attributeNodes = { // 엔티티 매니저 그래프
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")})
@NamedEntityGraph(name = "Study.withTagsAndManagers", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("managers")})
@NamedEntityGraph(name = "Study.withZonesAndManagers", attributeNodes = {
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers")})
@NamedEntityGraph(name = "Study.withManagers", attributeNodes = {
        @NamedAttributeNode("managers")})
@NamedEntityGraph(name = "Study.withMembers", attributeNodes = {
        @NamedAttributeNode("members")})
@NamedEntityGraph(name = "Study.withTagsAndZones", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("zones")})
@NamedEntityGraph(name = "Study.withManagersAndMembers", attributeNodes = {
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")})
@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Study {

    @Id @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob @Basic(fetch = FetchType.LAZY) // 스터디 정보 조회할 때 다 가져오도록 함
    private String fullDescription;

    @Lob @Basic(fetch = FetchType.LAZY)
    private String image; // 프로필 이미지

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Zone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    private int memberCount;

    public void addManager(Account account) {
        this.managers.add(account);
    }

    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.isPublished() && this.isRecruiting()
                && !this.members.contains(account) && !this.managers.contains(account);
    }

    public boolean isMember(UserAccount userAccount) {
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount) {
        return this.managers.contains(userAccount.getAccount());
    }

    public void publish() {
        if(!this.closed && !this.published) {
            this.published = true;
            this.publishedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("스터디를 공개할 수 없는 상태입니다. 스터디가 이미 공개됐거나 종료된 상태입니다.");
        }
    }

    public void close() {
        if(this.published && !this.closed) {
            this.closed = true;
            this.closedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("스터디를 종료할 수 없습니다. 스터디를 공개하지 않았거나 이미 종료된 스터디입니다.");
        }
    }

    public String getImage() {
        return image != null ? image : "/images/banner_image.png";
    }

    public void startRecruit() {
        if (canUpdateRecruiting()) {
            this.recruiting = true;
            this.recruitingUpdatedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("인원 모집을 시작할 수 없습니다. 스터디를 공개하거나 한 시간 뒤 다시 시도하세요.");
        }
    }

    public void stopRecruit() {
        if(canUpdateRecruiting()) {
            this.recruiting = false;
            this.recruitingUpdatedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("인원 모집을 멈출 수 없습니다. 스터디를 공개하거나 한 시간 뒤 다시 시도하세요.");
        }
    }

    public boolean canUpdateRecruiting() {
        return this.published && this.recruitingUpdatedDateTime == null || this.recruitingUpdatedDateTime.isBefore(LocalDateTime.now().minusHours(1));
    }

    public boolean isRemovable() {
        return !this.published; // TODO 모임을 했던 스터디는 삭제할 수 없다.
    }

    public void addMember(Account account) {
        this.getMembers().add(account);
        this.memberCount++;
    }

    public void removeMember(Account account) {
        this.getMembers().remove(account);
        this.memberCount--;
    }

    public String getEncodedPath() {
        return URLEncoder.encode(this.path, StandardCharsets.UTF_8);
    }

    public boolean isManagerOf(Account account) {
        return this.getManagers().contains(account);
    }
}
