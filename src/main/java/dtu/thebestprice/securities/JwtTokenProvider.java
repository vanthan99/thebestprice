package dtu.thebestprice.securities;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {
    @Autowired
    RedisTemplate template;

    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    public static final String JWT_SECRET = "thebestprice";

    //Thời gian có hiệu lực của chuỗi jwt
    private final long JWT_EXPIRATION = 604800000L;

    // Tạo ra jwt từ thông tin user
    public String generateToken(MyUserDetails userDetails) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)

                .claim("username", userDetails.getUsername())
                .claim("fullName", userDetails.getFullName())
                .claim("role", userDetails.getRoles())
                .claim("address", userDetails.getAddress())
                .claim("phoneNumber", userDetails.getPhoneNumber())
                .claim("status",userDetails.getStatus())
                .compact();
    }

    // Lấy thông tin user từ jwt
    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            if (template.hasKey(authToken)) {
                Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
                return true;
            } else log.error("Phiên đăng nhập đã hết hạn.Vui lòng đăng nhập lại");

        } catch (MalformedJwtException ex) {
//            log.error("Invalid JWT token");
            log.error("Chuỗi token không hợp lệ");
        } catch (ExpiredJwtException ex) {
//            log.error("Expired JWT token");
            log.error("Chuỗi token đã hết hạn");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
