package bytebrewers.bitpod.security;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.utils.dto.response.user.JwtClaim;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Service
public class JwtUtils {
    @Value("${app.bit-pods.jwt-secret")
    private String secretKey;
    
    @Value("${app.bit-pods.jwt-expiration}")
    private Long expiretionInSecond;

    @Value("${app.bit-pods.jwt-app-name}")
    private String appName;

     public String generateToken(User user) {
        try {
            List<String> roles = user.getRoles().stream().map(role -> role.getRole().name()).toList();
            return JWT.create()
                    .withIssuer(appName)
                    .withSubject(user.getId())
                    .withExpiresAt(Instant.now().plusSeconds(expiretionInSecond))
                    .withClaim("roles", roles)
                    .sign(Algorithm.HMAC512(secretKey));
        } catch (JWTCreationException e) {
            log.error("Error while creating JWT token", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean verifyJwtToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getIssuer().equals(appName);
        } catch (JWTVerificationException e){
            log.error("Invalid JWT signature: {}", e.getMessage());
            return false;
        } 
    }

    public JwtClaim getUserInfoByToken (String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
            return JwtClaim.builder()
                    .userId(decodedJWT.getSubject())
                    .roles(roles)
                    .build();
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
