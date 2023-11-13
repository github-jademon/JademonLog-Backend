package com.example.JademonLog.controller;

import com.example.JademonLog.dto.token.TokenDto;
import com.example.JademonLog.dto.token.TokenRequestDto;
import com.example.JademonLog.dto.member.MemberRequestDto;
import com.example.JademonLog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody MemberRequestDto memberRequestDto, BindingResult errors) {
        return authService.signup(memberRequestDto, errors);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberRequestDto memberRequestDto) {
        return authService.login(memberRequestDto);
    }

}