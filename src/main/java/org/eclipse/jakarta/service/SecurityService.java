package org.eclipse.jakarta.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.shiro.lang.codec.Hex;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.openid.Claims;

@RequestScoped
public class SecurityService {

  @Inject
  private QueryService queryService;

  public Key generateKey(String keyString) {
   return new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
  }

  public boolean authenticateUser(String email, String password) {
    return queryService.authenticateUser(email,password) ;
  }

  public Date toDate(LocalDateTime dateTime) {
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public boolean passwordMatch(String dbHashedPassword, String saltText, String plainPassword){
     byte[] salt = Hex.decode(saltText);
     String hashedPassword = hashAndSaltPassword(plainPassword, salt);
     return Arrays.equals(Hex.decode(dbHashedPassword), Hex.decode(hashedPassword));
  }

  public Map<String, String> hashPassword(String plainPassword){
    byte[] salt = getSalt();
    Map<String, String> credMap = new HashMap<>();
    String hashedPassword = hashAndSaltPassword(plainPassword, salt);
    credMap.put("hashedPassword", bytesToBase64(hashedPassword.getBytes())); // Convierte el hash a HEX
    credMap.put("salt", bytesToHex(salt));
    return credMap;
  }

  private String hashAndSaltPassword(String plainPassword, byte[] salt) {
    try{
      int iterations = 13000;
      int keyLength = 512;
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
      PBEKeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, iterations, keyLength);
      SecretKey key = factory.generateSecret( spec );
      byte[] hashedPassword = key.getEncoded();
      return bytesToBase64(hashedPassword);
    }catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
  }

  private byte[] getSalt() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] salt = new byte[16]; 
    secureRandom.nextBytes(salt);
    return salt;
}

  public String generateToken(String email) {
        Key key = generateKey(email);
        byte[] keyBytes = key.getEncoded();
        Algorithm algorithm = Algorithm.HMAC256(keyBytes);

        
        return JWT.create()
                .withSubject("User Authentication") 
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .sign(algorithm);
  }

  private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
        sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private String bytesToBase64(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  public DecodedJWT validateTokenAndDecodeToken(String token, Key key) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(key.getEncoded()); 
            JWTVerifier verifier = JWT.require(algorithm)
                .build(); 

            DecodedJWT jwt = verifier.verify(token); 

            Date expirationDate = jwt.getExpiresAt();
            if (expirationDate != null && expirationDate.before(new Date())) {
                return null; 
            }
            
            return jwt; 

        } catch (Exception e) {
            
            return null; 
        }
    }

}
