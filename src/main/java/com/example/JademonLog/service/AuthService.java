package com.example.JademonLog.service;

import com.example.JademonLog.dto.member.MemberRequestDto;
import com.example.JademonLog.dto.member.MemberResponseDto;
import com.example.JademonLog.dto.token.TokenDto;
import com.example.JademonLog.entity.member.Member;
import com.example.JademonLog.entity.member.MemberRepository;
import com.example.JademonLog.jwt.TokenProvider;
import com.example.JademonLog.response.BasicResponse;
import com.example.JademonLog.response.ErrorResponse;
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
    public static final BasicResponse res = new BasicResponse().success("ok", null);

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
                res.setMessage("이미 가입되어 있는 유저입니다");
                throw new RuntimeException(res.getMessage());
            }
            else if(!passwordDuplicate(memberRequestDto.getPassword(), memberRequestDto.getPasswordck())) {
                res.setMessage("비밀번호가 다릅니다");
                throw new RuntimeException(res.getMessage());
            }

            res.setMessage("회원가입이 완료되었습니다");
            Member user = memberRequestDto.toMember(passwordEncoder);

            data.put("data", MemberResponseDto.of(memberRepository.save(user)));

            res.setResult(data);

            return new ResponseEntity(res, res.getHttpStatus());

        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity login(MemberRequestDto memberRequestDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            data.put("token", tokenDto);

            res.setResult(data);
            return new ResponseEntity(res, res.getHttpStatus());
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
