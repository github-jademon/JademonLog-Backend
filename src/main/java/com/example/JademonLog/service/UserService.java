package com.example.JademonLog.service;

import com.example.JademonLog.dto.member.MemberResponseDto;
import com.example.JademonLog.entity.member.Member;
import com.example.JademonLog.entity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final MemberRepository memberRepository;

    public MemberResponseDto findMemberInfoById(Long userId) {
        return memberRepository.findById(userId)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public MemberResponseDto findMemberInfoByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    public MemberResponseDto findMemberInfoByUserid(String userid) {
        return memberRepository.findByUserid(userid)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    public Member getMember(Long userId) {
        try {
            if(userId==null) throw new Exception();
            return memberRepository.findById(userId).get();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Member getEmailMember(String email) {
        try {
            return memberRepository.findByEmail(email).get();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}