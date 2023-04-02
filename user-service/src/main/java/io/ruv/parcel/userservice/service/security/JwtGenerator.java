package io.ruv.parcel.userservice.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.ruv.parcel.userservice.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtGenerator {

    @Value("${jwt.signing.key.path}")
    private String privateKeyPath;
    @Value("${jwt.token.expiry.ms}")
    private long expiryMs;
    private Key key;
    private final ObjectMapper objectMapper;


    @PostConstruct
    public void readKey() throws IOException {

        try (var reader = Files.newBufferedReader(ResourceUtils.getFile(privateKeyPath).toPath());
             var parser = new PEMParser(reader)) {

            var keyConverter = new JcaPEMKeyConverter();

            var pemKeyPair = (PEMKeyPair) parser.readObject();
            key = keyConverter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
        }
    }

    public String generateJwt(User user) {

        try {

            return Jwts.builder()
                    .setId(UUID.randomUUID().toString())
                    .setClaims(Map.of("roles", objectMapper.writeValueAsString(user.getRoles())))
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(Instant.now(Clock.systemUTC()).toEpochMilli()))
                    .setExpiration(new Date(Instant.now(Clock.systemUTC()).plusMillis(expiryMs).toEpochMilli()))
                    .signWith(key, SignatureAlgorithm.RS256)
                    .compact();
        } catch (JsonProcessingException | RuntimeException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Failed to generate jwt for user '%s'.", user.getUsername()), e);
        }
    }
}
