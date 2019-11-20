package pl.piaseckif.ppmtool.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.piaseckif.ppmtool.domain.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static pl.piaseckif.ppmtool.services.SecurityConstants.EXPIRATION_TIME;
import static pl.piaseckif.ppmtool.services.SecurityConstants.SECRET_KEY;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime()+EXPIRATION_TIME);


        String userId =  Long.toString(user.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", (Long.toString(user.getId())));
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT Signature");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims token is empty");
        }
        return false;
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        Long id = Long.parseLong((String)claims.get("id"));
        return id;
    }
}
