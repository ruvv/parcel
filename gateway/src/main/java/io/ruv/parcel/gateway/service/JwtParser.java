package io.ruv.parcel.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.ruv.parcel.user.api.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JwtParser {

    @Value("${jwt.signing.key.path}")
    private String publicKeyPath;
    private Key key;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void readKey() throws IOException {

        try (var reader = Files.newBufferedReader(ResourceUtils.getFile(publicKeyPath).toPath());
             var parser = new PEMParser(reader)) {

            var keyConverter = new JcaPEMKeyConverter();

            var pemKey = SubjectPublicKeyInfo.getInstance(parser.readObject());

            key = keyConverter.getPublicKey(pemKey);
        }
    }

    public ParsedToken parse(String jwt) {

        try {

            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            var username = claims.getSubject();
            var roles = objectMapper.readValue((String) claims.get("roles"), new TypeReference<ArrayList<UserRole>>() {
            });

            var expiration = claims.getExpiration().toInstant();

            return new ParsedToken(username, roles, true, expiration.isBefore(Instant.now(Clock.systemUTC())));
        } catch (JsonProcessingException | RuntimeException e) {

            return new ParsedToken(null, null, false, false);
        }
    }


    public record ParsedToken(String username, List<UserRole> roles, boolean valid, boolean expired) {
    }
}

