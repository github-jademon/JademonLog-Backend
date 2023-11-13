package com.example.JademonLog.entity.member;

import com.example.JademonLog.entity.authority.Authority;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="users")
public class Member {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = false, name = "userid")
    private String userid;
    @Column(nullable = false, name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String email, String userid, String password, Authority authority) {
        this.email = email;
        this.userid = userid;
        this.password = password;
        this.authority = authority;
    }
}
