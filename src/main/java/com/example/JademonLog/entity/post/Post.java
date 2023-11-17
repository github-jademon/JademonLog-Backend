package com.example.JademonLog.entity.post;

import com.example.JademonLog.entity.image.Image;
import com.example.JademonLog.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
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

}
