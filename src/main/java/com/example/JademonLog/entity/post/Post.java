package com.example.JademonLog.entity.post;

import com.example.JademonLog.entity.image.Image;
import com.example.JademonLog.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    private LocalDateTime date;

    @OneToOne
    private Image image;

    @ManyToOne
    private Member writer;

    public Post(String title, String desc, String source, Member writer) {
        this.title = title;
        this.desc = desc;
        this.source = source;
        this.date = LocalDateTime.now();
        this.writer = writer;
    }


}
