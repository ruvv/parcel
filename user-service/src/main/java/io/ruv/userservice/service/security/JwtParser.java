//package io.ruv.userservice.service.security;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Jwts;
//import io.ruv.userservice.api.UserRole;
//import lombok.RequiredArgsConstructor;
//import org.bouncycastle.openssl.PEMKeyPair;
//import org.bouncycastle.openssl.PEMParser;
//import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ResourceUtils;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.security.Key;
//import java.time.Clock;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Supplier;
//
//todo evict this class
//@Service
//@RequiredArgsConstructor
//public class JwtParser {
//
//    private final Supplier<Key> keySupplier;
//    private final ObjectMapper objectMapper;
//
//
//    public ParsedToken parse(String jwt) {
//
//        try {
//
//            var claims = Jwts.parserBuilder()
//                    .setSigningKey(keySupplier.get())
//                    .build()
//                    .parseClaimsJws(jwt)
//                    .getBody();
//
//            var username = claims.getSubject();
//            var roles = objectMapper.readValue((String) claims.get("roles"), new TypeReference<ArrayList<UserRole>>() {
//            });
//
//            var expiration = claims.getExpiration().toInstant();
//
//            return new ParsedToken(username, roles, true, expiration.isBefore(Instant.now(Clock.systemUTC())));
//        } catch (JsonProcessingException | RuntimeException e) {
//
//            return new ParsedToken(null, List.of(), false, false);
//        }
//    }
//
//
//    public record ParsedToken(String username, List<UserRole> roles, boolean valid, boolean expired) {
//    }
//
//    public static void main(String args[]) throws IOException {
//
//        var token = "eyJhbGciOiJSUzI1NiJ9.eyJyb2xlcyI6IltcIkNVU1RPTUVSXCJdIiwic3ViIjoiYWxpY2UiLCJpYXQiOjE2Nzk5Mzc2MjUs" +
//                "ImV4cCI6MTY3OTkzODIyNX0.mWmKTMQ8cU6WsjCXPkJS1-7qdtfx2P07cudrhBCNxZ-i3QBP6e0E9W3bZjaJN9--aabDe5WyOGKMg" +
//                "m0OHzqDsBX0Do0frvnhALuTHrH42LJu9m45jqTCKNhhP_EcC2PeFLs2w0Xr_c452qpzOz_9lmOnLk2H4i-3zGhTGwAFMCzfgDJv8W" +
//                "T9nRSf429II8I6p4HfgX5_JlsB1be01WmnDxi2haB0TIoMpKR3GCrnl3k7kMc2S8SoTmZnbsw2vueeFTX4Sgi-RHnswF77wV0pH47" +
//                "i4f_1dRcSs_p1Pj8uup87r2lVSTYAyCB8Csi7O3uDYvKbnaS8zbyrRAISz7nkkzv6rD-oBvhO0s1hOSvUlaG_yNgIioV18LeDUiJw" +
//                "JjOUezlpIHH3WZK8_UmORvCWoSeNTI4rwWcvwbyLe_Ci5JuN4a5V4YVNk4Zn-4w9dQUIdhMkFItGjzMqGnG6G1s-sosZJSl9s6PXE" +
//                "_60Ghm29uCS0xlJLUHLcglIGoG0SvI-1vtubZDbjV_LXjsUL9KTrzN7LyPMPzsAwpx7N7Y_HYRUw00EL0cdAmpCRpraBRrBrvRm4b" +
//                "UTESGPxDUjPbP-jIe09n62Bc-PTwSEbaoGFlxf8khfjkfpCn_h66mMsjijs2AEOFQui9iufotWNyALldLkwREG29IWwJ3OX8j9uDI";
//
//        var key = readKey();
//
//        var claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        var username = claims.getSubject();
//        var roles = new ObjectMapper().readValue((String) claims.get("roles"), new TypeReference<ArrayList<UserRole>>() {
//        });
//
//        var expiration = claims.getExpiration().toInstant();
//
//        var parsed = new ParsedToken(username, roles, true, expiration.isBefore(Instant.now(Clock.systemUTC())));
//
//        System.out.println(parsed);
//    }
//
//    public static Key readKey() throws IOException {
//
//        try (var reader = Files.newBufferedReader(ResourceUtils.getFile("classpath:jwtRS256.key").toPath());
//             var parser = new PEMParser(reader)) {
//
//            var keyConverter = new JcaPEMKeyConverter();
//
//            var pemKeyPair = (PEMKeyPair) parser.readObject();
//            return keyConverter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
//        }
//    }
//}

