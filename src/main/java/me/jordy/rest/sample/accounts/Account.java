package me.jordy.rest.sample.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of ="id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @GeneratedValue @Id
    private Integer id;

    @Column(unique = true)
    private String email;

    private String password;

    /* 복수의 엘리먼트를 가져올 수 있음 */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<AccountRole> roles;
}
