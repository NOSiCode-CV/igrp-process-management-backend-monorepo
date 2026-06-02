package cv.igrp.framework.process.runtime.auth.irn.adapter;

import cv.igrp.framework.process.runtime.auth.irn.adapter.integration.IrnAuthClient;
import cv.igrp.framework.process.runtime.auth.irn.adapter.integration.data.IrnMeResponse;
import cv.igrp.framework.process.runtime.auth.irn.adapter.integration.data.UserSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Internal service for cached IRN authorization operations.
 * Separated from the main adapter to ensure proper Spring AOP proxy behavior for caching.
 */
@Service
public class IrnAuthorizationCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrnAuthorizationCacheService.class);

    private final IrnAuthClient client;
    private final String superAdminEmail;

    public IrnAuthorizationCacheService(IrnAuthClient client, cv.igrp.framework.process.runtime.auth.irn.adapter.integration.config.IrnApiProperties properties) {
        this.client = client;
        this.superAdminEmail = properties.superAdminEmail();
    }

    @Cacheable(value = "rolesCache", key = "#sessionId", unless = "#result.isEmpty()")
    public Set<String> getRoles(String sessionId) {
        try {
            LOGGER.debug("Getting roles for current user with sessionId: {}", sessionId);

            if (sessionId == null || sessionId.isBlank()) {
                LOGGER.warn("getRoles: Session ID is null or empty");
                return Set.of();
            }

            var irnMeResponse = client.getMe(sessionId);
            Set<String> roles = extractAllProfiles(irnMeResponse);

            LOGGER.debug("Current User Roles: {}", roles);

            return roles;

        } catch (Exception e) {
            LOGGER.error("Error getting roles for current user", e);
            return Set.of();
        }
    }

    @Cacheable(value = "permissionsCache", key = "#sessionId", unless = "#result.isEmpty()")
    public Set<String> getPermissions(String sessionId) {
        try {
            LOGGER.debug("Getting permissions for current user with sessionId: {}", sessionId);

            // Note: Permissions are not currently enabled in IRN implementation.
            // To enable permissions management, uncomment the code below:
            //
            // if (sessionId != null && !sessionId.isBlank()) {
            //     var irnMeResponse = client.getMe(sessionId);
            //     Set<String> permissions = new HashSet<>(irnMeResponse.permissions());
            //     LOGGER.debug("Permissions: {}", permissions);
            //     return permissions;
            // }

            LOGGER.debug("getPermissions: Permissions not enabled, returning empty set.");
            return Set.of();

        } catch (Exception e) {
            LOGGER.error("Error getting permissions for current user", e);
            return Set.of();
        }
    }

    @Cacheable(value = "departmentsCache", key = "#sessionId", unless = "#result.isEmpty()")
    public Set<String> getDepartments(String sessionId) {
        try {
            LOGGER.debug("Getting departments for current user with sessionId: {}", sessionId);

            if (sessionId == null || sessionId.isBlank()) {
                LOGGER.warn("getDepartments: Session ID is null or empty");
                return Set.of();
            }

            var irnMeResponse = client.getMe(sessionId);
            Set<String> departments = new HashSet<>(extractSelectedSpaceData(irnMeResponse));

            LOGGER.debug("Departments: {}", departments);

            return departments;

        } catch (Exception e) {
            LOGGER.error("Error getting departments for current user", e);
            return Set.of();
        }
    }

    @Cacheable(value = "superAdminCache", key = "#sessionId")
    public boolean isSuperAdmin(String sessionId) {
        try {
            LOGGER.debug("Checking if current user is super admin with sessionId: {}", sessionId);

            if (sessionId == null || sessionId.isBlank()) {
                LOGGER.warn("isSuperAdmin: Session ID is null or empty");
                return false;
            }

            var irnMeResponse = client.getMe(sessionId);

            if (irnMeResponse == null || irnMeResponse.email() == null) {
                LOGGER.warn("isSuperAdmin: Invalid response or null email from IRN API");
                return false;
            }

            var isSuperAdmin = irnMeResponse.email().equals(superAdminEmail);

            LOGGER.debug("Is User SuperAdmin: {}", isSuperAdmin);

            return isSuperAdmin;

        } catch (Exception e) {
            LOGGER.error("Error checking if current user is super admin", e);
            return false;
        }
    }

    /**
     * Extracts profile IDs from the IRN API response.
     * Currently only returns the selected profile. To include all user space profiles,
     * uncomment the code section below and modify the return statement.
     *
     * @param response the IRN API response
     * @return set of profile IDs
     */
    protected Set<String> extractAllProfiles(IrnMeResponse response) {
        if (response == null) {
            LOGGER.warn("extractAllProfiles: Received null response");
            return Set.of();
        }

        // Note: Currently only returning selected profile.
        // To include all user space profiles, uncomment this section:
        //
        // Stream<String> spaceProfiles =
        //     response.userSpaces() == null ? Stream.empty()
        //         : response.userSpaces().stream()
        //             .flatMap(space -> Stream.concat(
        //                 Stream.ofNullable(space.profileCode()),
        //                 space.profiles() == null
        //                     ? Stream.empty()
        //                     : space.profiles().stream()
        //                         .map(Profile::profileCode)
        //             ));

        Stream<String> selectedProfile =
                response.selectedProfile() != null
                        ? Stream.ofNullable(response.selectedProfile().profileCode())
                        : Stream.empty();

        return selectedProfile
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Extracts space-related data from the user's selected space.
     * Returns a list containing space ID, code, registry office ID, and conservatoria code.
     *
     * @param response the IRN API response
     * @return list of space-related identifiers, or empty list if not available
     */
    protected List<String> extractSelectedSpaceData(IrnMeResponse response) {

        if (response == null || response.selectedSpace() == null) {
            LOGGER.warn("extractSelectedSpaceData: Received null response or null selectedSpace");
            return List.of();
        }

        UserSpace space = response.selectedSpace();

        return Stream.of(
                        space.spaceId(),
                        space.spaceCode(),
                        space.registryOfficeId(),
                        space.conservatoriaCode()
                )
                .filter(Objects::nonNull)
                .toList();
    }
}
