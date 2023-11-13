package com.example.JademonLog.service;

import com.example.JademonLog.dto.member.MemberRequestDto;
import com.example.JademonLog.dto.member.MemberResponseDto;
import com.example.JademonLog.dto.token.TokenDto;
import com.example.JademonLog.entity.member.Member;
import com.example.JademonLog.entity.member.MemberRepository;
import com.example.JademonLog.jwt.TokenProvider;
import com.example.JademonLog.response.DefaultRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    public static final DefaultRes res = new DefaultRes<>(HttpStatus.OK, HttpStatus.OK.value(), "ok");
    public static HashMap<String, Object> data = new HashMap<>();

    public boolean passwordDuplicate(String password, String passwordCk) {
        return password.equals(passwordCk);
    }

    @Transactional
    public ResponseEntity signup(MemberRequestDto memberRequestDto, BindingResult errors) {
        try {
            if(errors.hasErrors()) {
                Map<String, String> validatorResult = new HashMap<>();

                for (FieldError error : errors.getFieldErrors()) {
                    String validKeyName = String.format("valid_%s", error.getField());
                    validatorResult.put(validKeyName, error.getDefaultMessage());
                }

                return new ResponseEntity(validatorResult, HttpStatus.OK);
            }

            if(memberRepository.findByEmail(memberRequestDto.getEmail()).isPresent()) {
                res.setResponseMessage("이미 가입되어 있는 유저입니다");
                throw new RuntimeException(res.getResponseMessage());
            }
            else if(!passwordDuplicate(memberRequestDto.getPassword(), memberRequestDto.getPasswordck())) {
                res.setResponseMessage("비밀번호가 다릅니다");
                throw new RuntimeException(res.getResponseMessage());
            }

            res.setResponseMessage("회원가입이 완료되었습니다");
            Member user = memberRequestDto.toMember(passwordEncoder);

            data.put("data", MemberResponseDto.of(memberRepository.save(user)));

            DefaultRes response = DefaultRes.res(HttpStatus.OK, HttpStatus.OK.value(), res.getResponseMessage(), data);

            return new ResponseEntity(response, response.getHttpStatus());

        } catch(Exception e) {
            e.printStackTrace();
            DefaultRes response = DefaultRes.res(HttpStatus.OK, HttpStatus.OK.value(), res.getResponseMessage());
            return new ResponseEntity(response, response.getHttpStatus());
        }
    }

    @Transactional
    public ResponseEntity login(MemberRequestDto memberRequestDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            data.put("token", tokenDto);

            DefaultRes response = DefaultRes.res(HttpStatus.OK, HttpStatus.OK.value(), res.getResponseMessage(), data);
            return new ResponseEntity(response, response.getHttpStatus());
        } catch(Exception e) {
            e.printStackTrace();

            DefaultRes response = DefaultRes.res(HttpStatus.OK, HttpStatus.OK.value(), res.getResponseMessage());
            return new ResponseEntity(response, response.getHttpStatus());
        }
    }
}
