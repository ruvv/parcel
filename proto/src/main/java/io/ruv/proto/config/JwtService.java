package io.ruv.proto.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

//@Service
//public class JwtService {
//
//    //    private Path publicKeyFile = Path.of("classpath:jwtRS256.key.pub");
//    private Path privateKeyFile = Path.of("classpath:jwtRS256.key");
//
//    private long expiryMs = 3600000;
//
//    private Key pubKey;
//    private Key privateKey;
//
//    @PostConstruct
//    public void readKeys() throws IOException {
//
//        try (var reader = Files.newBufferedReader(Path.of("classpath:jwtRS256.key"));
//             var parser = new PEMParser(reader)) {
//
//            var keyConverter = new JcaPEMKeyConverter();
//
//            var pemKeyPair = (PEMKeyPair) parser.readObject();
//            this.pubKey = keyConverter.getPublicKey(pemKeyPair.getPublicKeyInfo());
//            this.privateKey = keyConverter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
//        }
//    }
//
//    private String generate(UserDetails userDetails) {
//
//        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime() + expiryMs))
//                .signWith(pubKey, SignatureAlgorithm.RS256)
//                .compact();
//    }
//
//    private String extractUsername(String jwt) {
//
//        return extractClaim(jwt, Claims::getSubject);
//    }
//
//    private Claims claims(String jwt) {
//
//        return Jwts.parserBuilder().setSigningKey(privateKey).build()
//                .parseClaimsJws(jwt).getBody();
//    }
//
//    private <T> T extractClaim(String jwt, Function<Claims, T> resolver) {
//
//        return resolver.apply(claims(jwt));
//    }
//}
