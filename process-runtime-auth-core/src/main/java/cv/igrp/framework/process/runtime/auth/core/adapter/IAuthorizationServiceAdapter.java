package cv.igrp.framework.process.runtime.auth.core.adapter;


import jakarta.servlet.http.HttpServletRequest;


import java.util.Set;

public interface IAuthorizationServiceAdapter {

	Set<String> getGroups(String jwt, HttpServletRequest request);

	Set<String> getPermissions(String jwt, HttpServletRequest request);

	boolean isSuperAdmin(String jwt, HttpServletRequest request);

	Set<String> getActiveGroups(String jwt, HttpServletRequest request);

}
