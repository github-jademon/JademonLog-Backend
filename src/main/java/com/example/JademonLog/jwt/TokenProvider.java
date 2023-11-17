package com.example.JademonLog.jwt;

import com.example.JademonLog.dto.token.TokenDto;
import com.example.JademonLog.entity.member.Member;
import com.example.JademonLog.entity.member.MemberRepository;
import com.example.JademonLog.service.AuthService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000*60*30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000*60*60*24*7; // 7일
    private final Key key;
    private final MemberRepository memberRepository;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        System.out.println(accessTokenExpiresIn);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(JwtProperties.AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        return TokenDto.builder()
                .grantType(JwtProperties.BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if(claims.get(JwtProperties.AUTHORITIES_KEY) == null) {
            AuthService.res.setMessage("권한 정보가 없는 토큰입니다.");
            throw new RuntimeException(AuthService.res.getMessage());
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtProperties.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Authorization");
    }

    public Optional<Member> getMemberByToken(HttpServletRequest httpServletRequest) {
        try {
            String bearerToken = resolveToken(httpServletRequest);

            if (!validateToken(bearerToken)) {
                return Optional.empty();
            }

            String token = bearerToken.split(" ")[1].trim();
            String si_number = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            log.info("si_number: {}", si_number);

            return memberRepository.findById(Long.parseLong(si_number));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            AuthService.res.setMessage("잘못된 JWT 서명입니다.");
            log.info(AuthService.res.getMessage());
        } catch (ExpiredJwtException e) {
            AuthService.res.setMessage("만료된 jWT 토큰입니다.");
            log.info(AuthService.res.getMessage());
        } catch (UnsupportedJwtException e) {
            AuthService.res.setMessage("지원되지 않는 JWT 토큰입니다.");
            log.info(AuthService.res.getMessage());
        } catch(IllegalArgumentException e) {
            AuthService.res.setMessage("JWT 토큰이 잘못되었습니다.");
            log.info(AuthService.res.getMessage());
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch(ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
