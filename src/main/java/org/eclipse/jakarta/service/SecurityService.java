package org.eclipse.jakarta.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.lang.codec.Hex;
import org.apache.shiro.lang.util.ByteSource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class SecurityService {

  @Inject
  private QueryService queryService;

  private Key generateKey(String keyString) {
   return new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
  }

  public boolean authenticateUser(String email, String password) {
    return queryService.authenticateUser(email,password) ;
  }

  public Date toDate(LocalDateTime dateTime) {
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public boolean passwordMatch(String dbHashedPassword, String saltText, String plainPassword){
     ByteSource salt = ByteSource.Util.bytes(Hex.decode(saltText));
     String hashedPassword = hashAndSaltPassword(plainPassword, salt);
     return hashedPassword.equals(dbHashedPassword);
  }

  public Map<String, String> hashPassword(String plainPassword){
    ByteSource salt = getSalt();
    Map<String, String> credMap = new HashMap<>();
    credMap.put("hashedPassword",hashAndSaltPassword(plainPassword, salt));
    credMap.put("salt", salt.toHex());
    return credMap;
  }

  private String hashAndSaltPassword(String plainPassword, ByteSource salt){
    return new Sha512Hash(plainPassword, salt, 2000000)
    .toHex();
  }

  private ByteSource getSalt(){
    return new SecureRandomNumberGenerator()
    .nextBytes();
  }

  public String generateToken(String email) {
        Key key = generateKey(email);
        byte[] keyBytes = key.getEncoded();
        Algorithm algorithm = Algorithm.HMAC256(keyBytes);

        
        return JWT.create()
                .withSubject(email) 
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .sign(algorithm);
  }

}
