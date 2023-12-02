package io.intino.alexandria.ui.displays.components.actionable;

public interface SignInfoProvider {
	String company();
	String email();
	String secret();

	default boolean canSetup() {
		return true;
	}

	default SignInfo signInfo() {
		return new SignInfo().company(company()).email(email()).secret(secret()).canSetup(canSetup());
	}
}
