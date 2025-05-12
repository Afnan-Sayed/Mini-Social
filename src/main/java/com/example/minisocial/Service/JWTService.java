//package com.example.minisocial.Service;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.SignatureAlgorithm;
//import jakarta.ejb.Stateless;
//
//import java.util.Base64;
//
//@Stateless
//public class JWTService
//{
//    private static final String SECRET_KEY = "your-secret-key";
//
//    public String getEmailFromToken(String token)
//    {
//        Claims claims = Jwts.parser()
//                .setSigningKey(Base64.getDecoder().decode(SECRET_KEY))
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.getSubject();  //malak 3amlha enno email
//    }
//}
//
///*
//import java.util.Base64;
//
//public class JWTService {
//
//    private static final String SECRET_KEY = "your-secret-key";
//
//    public String getEmailFromToken(String token) {
//        // Decode the token using URL-safe base64
//        String[] parts = token.split("\\."); // Split the JWT token into 3 parts
//
//        // Decode the payload (second part of the JWT token)
//        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
//
//        // Parse the payload (you can use a JSON library to parse the payload)
//        // For example, use JSONObject to parse the decoded payload to extract email
//        JSONObject jsonObject = new JSONObject(payload);
//        return jsonObject.getString("sub");  // Assuming 'sub' is the email field
//    }
//}
//
// */