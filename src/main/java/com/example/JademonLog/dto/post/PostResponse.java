package com.example.JademonLog.dto.post;

import com.example.JademonLog.entity.image.Image;
import com.example.JademonLog.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PostResponse {
    private Long id;

    private String title;

    private String source;

    private LocalDateTime date;

    private Image image;

    private Member writer;
}
