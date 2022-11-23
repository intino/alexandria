package io.intino.alexandria.ui.displays.components.actionable;

public interface SignInfoProvider {
	String company();
	String email();
	String secret();

	default SignInfo signInfo() {
		return new SignInfo().company(company()).email(email()).secret(secret());
	}
}
