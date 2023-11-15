package com.example.JademonLog.entity.post;

import com.example.JademonLog.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String desc;

    private String source;

    private String date;

    @OneToOne
    private Member writer;

    public Post(String title, String desc, String source, Member writer) {
//        this.
    }


}
