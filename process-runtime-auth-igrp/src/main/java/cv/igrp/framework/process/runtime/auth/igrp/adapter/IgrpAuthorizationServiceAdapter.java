package cv.igrp.framework.process.runtime.auth.igrp.adapter;


import cv.igrp.framework.process.runtime.auth.core.adapter.IAuthorizationServiceAdapter;
import cv.igrp.platform.access.client.ApiClient;
import cv.igrp.platform.access.client.api.DepartmentsApi;
import cv.igrp.platform.access.client.api.UsersApi;
import cv.igrp.platform.access.client.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@ConditionalOnProperty(
		name = "igrp.authorization.service.adapter",
		havingValue = "igrp"
)
public class IgrpAuthorizationServiceAdapter implements IAuthorizationServiceAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(IgrpAuthorizationServiceAdapter.class);

	private final ApiClient client;

	public IgrpAuthorizationServiceAdapter(ApiClient client) {
		this.client = client;
	}

	@Override
	@Cacheable(value = "rolesCache", key = "#jwt", unless = "#result.isEmpty()")
	public Set<String> getRoles(String jwt, HttpServletRequest request) {
		try {

			LOGGER.debug("Getting roles for current user");

			client.setAuthToken(jwt);

			var usersApi = new UsersApi(client);

			List<RoleDTO> currentUserRoles = usersApi.getCurrentUserRoles();
			LOGGER.debug("Current User Roles: {}", currentUserRoles);

			Set<String> roles = currentUserRoles.stream()
					.map(roleDTO -> normalizeRoleCode(roleDTO.getDepartmentCode(), roleDTO.getCode()))
					.collect(Collectors.toSet());

			// Descendant roles
			currentUserRoles.forEach(role -> roles.addAll(getDescendantRoles(role.getDepartmentCode(), role.getCode())));

			return roles;

		} catch (Exception e) {
			LOGGER.error("Error getting roles for current user", e);
			return Set.of();
		}

	}

	private String normalizeRoleCode(String departmentCode, String roleCode) {
		if (departmentCode == null) {
			LOGGER.warn("Role {} has no department code", roleCode);
			return roleCode;
		}
		String prefix = departmentCode + ".";
		return roleCode.startsWith(prefix) ? roleCode : prefix + roleCode;
	}

	private Set<String> getDescendantRoles(String departmentCode, String roleCode) {
		Set<String> descendantRoles = new HashSet<>();
		try {
			var departmentsApi = new DepartmentsApi(client);
			RoleChildHierarchyDTO roleChildHierarchyDTO = departmentsApi.getRoleChildren(
					departmentCode,
					roleCode,
					null
			);
			LOGGER.debug("Role Children: {}", roleChildHierarchyDTO);
			if (roleChildHierarchyDTO != null && roleChildHierarchyDTO.getChildren() != null) {
				roleChildHierarchyDTO.getChildren()
						.forEach(childRole ->
								descendantRoles.add(normalizeRoleCode(childRole.getDepartmentCode(), childRole.getRoleCode())));
			}
		} catch (Exception e) {
			LOGGER.error("Error getting role children for role {}", roleCode, e);
		}
		return descendantRoles;
	}

	@Override
	@Cacheable(value = "permissionsCache", key = "#jwt", unless = "#result.isEmpty()")
	public Set<String> getPermissions(String jwt, HttpServletRequest request) {
		try {

			LOGGER.debug("Getting permissions for current user");

			client.setAuthToken(jwt);

			var usersApi = new UsersApi(client);

			List<PermissionDTO> permissions = usersApi.getCurrentUserPermissions(null);
			LOGGER.debug("Permissions: {}", permissions);

			return permissions.stream()
					.map(PermissionDTO::getName)
					.collect(Collectors.toSet());

		} catch (Exception e) {
			LOGGER.error("Error getting permissions for current user", e);
			return Set.of();
		}

	}

	@Override
	@Cacheable(value = "departmentsCache", key = "#jwt", unless = "#result.isEmpty()")
	public Set<String> getDepartments(String jwt, HttpServletRequest request) {
		try {

			LOGGER.debug("Getting departments for current user");

			client.setAuthToken(jwt);

			var usersApi = new UsersApi(client);

			List<DepartmentDTO> departments = usersApi.getCurrentUserDepartments();
			LOGGER.debug("Departments: {}", departments);

			return departments.stream()
					.map(DepartmentDTO::getCode)
					.collect(Collectors.toSet());

		} catch (Exception e) {
			LOGGER.error("Error getting departments for current user", e);
			return Set.of();
		}

	}

	@Override
	@Cacheable(value = "superAdminCache", key = "#jwt")
	public boolean isSuperAdmin(String jwt, HttpServletRequest request) {
		try {

			LOGGER.debug("Checking if current user is super admin");

			client.setAuthToken(jwt);
			var usersApi = new UsersApi(client);
			boolean isSuperAdmin = usersApi.isSuperadmin();

			LOGGER.debug("Current user is super admin: {}", isSuperAdmin);

			return isSuperAdmin;

		} catch (Exception e) {
			LOGGER.error("Error checking if current user is super admin", e);
		}
		return false;
	}

	@Override
	@Cacheable(value = "activeRoleCache", key = "#jwt")
	public Set<String> getActiveRoles(String jwt, HttpServletRequest request) {

		LOGGER.debug("Get active roles for current user");

		Set<String> roles = new HashSet<>();

		try {

			client.setAuthToken(jwt);
			var usersApi = new UsersApi(client);
			RoleDepartmentDTO currentRole = usersApi.getCurrentUserActiveRole();

			LOGGER.debug("Current active role: {}", currentRole);

			if (currentRole != null) {
				roles.add(normalizeRoleCode(currentRole.getDepartmentCode(), currentRole.getRoleCode()));
				// Descendant roles
				roles.addAll(getDescendantRoles(currentRole.getDepartmentCode(), currentRole.getRoleCode()));
			}

		} catch (Exception e) {
			LOGGER.error("Error getting active roles for current user", e);
		}

		return roles;
	}

}
