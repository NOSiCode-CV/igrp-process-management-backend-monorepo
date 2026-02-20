package cv.igrp.framework.process.runtime.auth.core.adapter;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

public interface IAuthorizationServiceAdapter {

  default Jwt parseJWT(String jwt) {
    return Jwt.withTokenValue(jwt).build();
  }

  Set<String> getRoles(String jwt, HttpServletRequest request);
  Set<String> getPermissions(String jwt, HttpServletRequest  request);
  Set<String> getDepartments(String jwt, HttpServletRequest  request);

  boolean isSuperAdmin(String jwt, HttpServletRequest  request);

  Set<String> getActiveRoles(String jwt, HttpServletRequest  request);

}
