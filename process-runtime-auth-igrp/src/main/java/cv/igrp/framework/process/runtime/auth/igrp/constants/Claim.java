package cv.igrp.framework.process.runtime.auth.igrp.constants;

public enum Claim {

	ROLES("roles"),
	SELECTED_ROLE("selectedRole"),
	SELECTED_ORG("org"),
	PERMISSIONS("permissions"),
	IS_SUPER_ADMIN("is_super_admin");

	private final String value;

	Claim(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
