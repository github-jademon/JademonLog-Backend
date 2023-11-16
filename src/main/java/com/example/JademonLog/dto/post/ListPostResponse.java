package com.example.JademonLog.dto.post;

import com.example.JademonLog.entity.image.Image;
import com.example.JademonLog.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ListPostResponse {
    private Long id;

    private String title;

    private String desc;

    private LocalDateTime date;

    private Image image;

    private Member writer;
}
