package me.brucehan.restfulhan.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // 하나의 enum만 있는 게 아니라 여러 개 enum을 가질 수 있으므로
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
