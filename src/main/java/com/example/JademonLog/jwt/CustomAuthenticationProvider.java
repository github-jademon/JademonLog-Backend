package com.example.JademonLog.jwt;

import com.example.JademonLog.service.AuthService;
import com.example.JademonLog.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) {
        AuthService.res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        AuthService.res.setResponseMessage("OK");
        try {
            if(authentication == null){
                AuthService.res.setResponseMessage("Authentication is null");
                throw new InternalAuthenticationServiceException(AuthService.res.getResponseMessage());
            }
            String username = authentication.getName();
            if(authentication.getCredentials() == null){
                AuthService.res.setResponseMessage("Credentials is null");
                throw new AuthenticationCredentialsNotFoundException(AuthService.res.getResponseMessage());
            }
            String password = authentication.getCredentials().toString();
            UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);
            if(loadedUser == null){
                AuthService.res.setResponseMessage("UserDetailsService returned null, which is an interface contract violation");
                throw new InternalAuthenticationServiceException(AuthService.res.getResponseMessage());
            }
            /* checker */
            if(!loadedUser.isAccountNonLocked()){
                AuthService.res.setResponseMessage("User account is locked");
                throw new LockedException(AuthService.res.getResponseMessage());
            }
            if(!loadedUser.isEnabled()){
                AuthService.res.setResponseMessage("User is disabled");
                throw new DisabledException(AuthService.res.getResponseMessage());
            }
            if(!loadedUser.isAccountNonExpired()){
                AuthService.res.setResponseMessage("User account has expired");
                throw new AccountExpiredException(AuthService.res.getResponseMessage());
            }
            /* 실질적인 인증 */
            if(!passwordEncoder.matches(password, loadedUser.getPassword())){
                AuthService.res.setStatusCode(HttpStatus.BAD_REQUEST.value());
                AuthService.res.setResponseMessage("Password does not match stored value");
                throw new BadCredentialsException(AuthService.res.getResponseMessage());
            }
            /* checker */
            if(!loadedUser.isCredentialsNonExpired()){
                AuthService.res.setResponseMessage("User credentials have expired");
                throw new CredentialsExpiredException(AuthService.res.getResponseMessage());
            }
            /* 인증 완료 */
            AuthService.res.setStatusCode(HttpStatus.OK.value());
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
            result.setDetails(authentication.getDetails());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}