package io.ruv.userservice.controller;

import io.ruv.userservice.api.auth.LoginDto;
import io.ruv.userservice.api.auth.TokenDto;
import io.ruv.userservice.service.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @Operation(summary = "User login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Login failed", content = @Content)
    })
    @PostMapping
    public TokenDto login(
            @Parameter(description = "User credentials") @RequestBody LoginDto loginDto) {

        return authService.tryAuthAndGetToken(loginDto);
    }
}
