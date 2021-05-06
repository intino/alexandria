package io.intino.alexandria.http.security;

public class NullSecurityManager implements AlexandriaSecurityManager {
	@Override
	public boolean check(String hash, String signature) {
		return true;
	}
}
