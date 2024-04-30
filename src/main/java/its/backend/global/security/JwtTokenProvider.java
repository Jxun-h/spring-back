package its.backend.global.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import its.backend.web.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final SecretKey key;
    private final RedisDao redisDao;

    // 유효한 시간을 일주일로 설정
    private static final int JWT_EXPIRATION_MS = 60480000;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RedisDao redisDao) {
        this.redisDao = redisDao;

//        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);

        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public TokenDto generateToken(Authentication authentication, Long userSeq) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities)
                .claim("userSeq", userSeq)
                .setIssuedAt(new Date())

                // 만료시간 30분 설정
                .setExpiration(new Date(now.getTime() + 30 * 60 * 1000L))

                // 암호화 알고리즘 종류
                // signature 에 사용될 secret 값 세팅
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisDao.setValues(authentication, refreshToken, JWT_EXPIRATION_MS + 5000L);

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 키 가져오기
    public Key getKey() {
        return key;
    }
}
