package io.ruv.userservice.service.security;

import io.ruv.userservice.api.auth.LoginDto;
import io.ruv.userservice.api.auth.TokenDto;
import io.ruv.userservice.entity.User;
import io.ruv.userservice.service.exception.BadCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    @Override
    public TokenDto tryAuthAndGetToken(LoginDto loginDto) {

        try {

            var user = (User) userDetailsService.loadUserByUsername(loginDto.getUsername());

            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {

                return new TokenDto(jwtGenerator.generateJwt(user));
            } else {

                throw new BadCredentialsException(loginDto.getUsername());
            }
        } catch (UsernameNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed", e);
        }
    }
}
