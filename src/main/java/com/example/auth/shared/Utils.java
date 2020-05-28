package com.example.auth.shared;

import com.example.auth.security.SecurityConstants;
import com.example.auth.shared.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

public class Utils {
    private static final Random random = new SecureRandom();
    private static final String ALPHABETS_DIGITS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String generateUserId(int length) {
        return generateUniqueId(length);
    }

    private static String generateUniqueId(int length) {
        StringBuilder uniqueId = new StringBuilder();
        for(int i=0;i<length;i++) {
            int index = random.nextInt(ALPHABETS_DIGITS.length());
            uniqueId.append(ALPHABETS_DIGITS.charAt(index));
        }
        return uniqueId.toString();
    }

    public static boolean isTokenValid(String token, String userId) {
        Jws<Claims> cliams  = Jwts.parser()
                .setSigningKey(SecurityConstants.TOKEN_SECRET)
                .parseClaimsJws(token);

        Date expirationDate = cliams.getBody().getExpiration();
        String userIdFromJWT = cliams.getBody().getSubject();

        return expirationDate.before(new Date()) && userId.equals(userIdFromJWT);
    }

    public static String generateToken(String publicUserId) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .setSubject(publicUserId)
                .compact();
    }
}
