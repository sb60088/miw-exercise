package com.miw.server.util;

import com.miw.server.domain.Jwt;
import com.miw.server.domain.User;
import com.miw.server.domain.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET = "MIW_SECRET_KEY";

    public static boolean isRegisteredUser(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String role = claims.get("userType", String.class);
        return StringUtils.isNotBlank(role) && role.equals(UserType.REGISTERED.name());
    }

    public static User getUser(String token) {
        String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
        return new User(user);
    }

    public static Jwt generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", UserType.REGISTERED.name());
        return new Jwt(Jwts.builder().addClaims(claims).setSubject(user.getName())
                .setIssuedAt(new Date())
                .setExpiration(DateUtils.addDays(new Date(), 1))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact());
    }
}
