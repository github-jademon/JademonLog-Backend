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
        AuthService.res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        AuthService.res.setMessage("OK");
        try {
            if(authentication == null){
                AuthService.res.setMessage("Authentication is null");
                throw new InternalAuthenticationServiceException(AuthService.res.getMessage());
            }
            String username = authentication.getName();
            if(authentication.getCredentials() == null){
                AuthService.res.setMessage("Credentials is null");
                throw new AuthenticationCredentialsNotFoundException(AuthService.res.getMessage());
            }
            String password = authentication.getCredentials().toString();
            UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);
            if(loadedUser == null){
                AuthService.res.setMessage("UserDetailsService returned null, which is an interface contract violation");
                throw new InternalAuthenticationServiceException(AuthService.res.getMessage());
            }
            /* checker */
            if(!loadedUser.isAccountNonLocked()){
                AuthService.res.setMessage("User account is locked");
                throw new LockedException(AuthService.res.getMessage());
            }
            if(!loadedUser.isEnabled()){
                AuthService.res.setMessage("User is disabled");
                throw new DisabledException(AuthService.res.getMessage());
            }
            if(!loadedUser.isAccountNonExpired()){
                AuthService.res.setMessage("User account has expired");
                throw new AccountExpiredException(AuthService.res.getMessage());
            }
            /* 실질적인 인증 */
            if(!passwordEncoder.matches(password, loadedUser.getPassword())){
                AuthService.res.setCode(HttpStatus.BAD_REQUEST.value());
                AuthService.res.setMessage("Password does not match stored value");
                throw new BadCredentialsException(AuthService.res.getMessage());
            }
            /* checker */
            if(!loadedUser.isCredentialsNonExpired()){
                AuthService.res.setMessage("User credentials have expired");
                throw new CredentialsExpiredException(AuthService.res.getMessage());
            }
            /* 인증 완료 */
            AuthService.res.setCode(HttpStatus.OK.value());
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