package io.ruv.parcel.userservice.service.security;

import io.ruv.parcel.user.api.auth.LoginDto;
import io.ruv.parcel.user.api.auth.TokenDto;

public interface AuthService {

    TokenDto tryAuthAndGetToken(LoginDto loginDto);
}
