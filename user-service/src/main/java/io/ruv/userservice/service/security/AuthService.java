package io.ruv.userservice.service.security;

import io.ruv.userservice.api.auth.LoginDto;
import io.ruv.userservice.api.auth.TokenDto;


public interface AuthService {

    TokenDto tryAuthAndGetToken(LoginDto loginDto);
}
