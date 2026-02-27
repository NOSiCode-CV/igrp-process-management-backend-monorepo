package cv.igrp.framework.process.runtime.auth.core.adapter;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConditionalOnProperty(
    name = "igrp.authorization.service.adapter",
    havingValue = "default",
    matchIfMissing = true
)
public class DefaultAuthorizationServiceAdapter implements IAuthorizationServiceAdapter{

  private static final Logger log = LoggerFactory.getLogger(DefaultAuthorizationServiceAdapter.class);

  @Override
  public Set<String> getRoles(String jwt, HttpServletRequest request) {
    log.debug("Fetching roles for user: {}", jwt);
    return Set.of();
  }

  @Override
  public Set<String> getPermissions(String jwt, HttpServletRequest request) {
    log.debug("Fetching permissions for user: {}", jwt);
    return Set.of();
  }

  @Override
  public Set<String> getDepartments(String jwt, HttpServletRequest request) {
    log.debug("Fetching departments for user: {}", jwt);
    return Set.of();
  }

  @Override
  public boolean isSuperAdmin(String jwt, HttpServletRequest request) {
    log.debug("Checking if user: {} is super admin", jwt);
    return false;
  }

  @Override
  public Set<String> getActiveRoles(String jwt, HttpServletRequest request) {
    log.debug("Fetching active roles for user: {}", jwt);
    return Set.of();
  }

}
