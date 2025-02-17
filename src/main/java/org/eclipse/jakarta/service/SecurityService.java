package org.eclipse.jakarta.service;

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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class SecurityService {

  @Inject
  private QueryService queryService;

  public Key generateKey(String keyString) {
    return new SecretKeySpec(keyString.getBytes(),0, keyString.getBytes().length, "DES");
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

}
