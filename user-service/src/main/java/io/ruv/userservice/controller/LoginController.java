package io.ruv.userservice.controller;

import io.ruv.userservice.api.auth.LoginDto;
import io.ruv.userservice.api.auth.TokenDto;
import io.ruv.userservice.service.exception.BadCredentialsException;
import io.ruv.userservice.service.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping
    public TokenDto login(@RequestBody LoginDto loginDto) throws UsernameNotFoundException, BadCredentialsException {

        return authService.tryAuthAndGetToken(loginDto);
    }
}
