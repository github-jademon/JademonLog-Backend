package com.example.JademonLog.dto.member;

import com.example.JademonLog.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {
    private String email;
    private String userid;

    public static MemberResponseDto of(Member user) {
        return new MemberResponseDto().builder()
                .email(user.getEmail())
                .userid(user.getUserid())
                .build();
    }
}