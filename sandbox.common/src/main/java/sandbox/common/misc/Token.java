package sandbox.common.misc;

import java.security.SecureRandom;

public class Token {
	public static final Integer LENGTH = 20;
	public final byte[] token;
	
	public Token(byte[] token) {
		this.token = token;
	}
	
	public Token() {
		token = new byte[LENGTH];
		new SecureRandom().nextBytes(token);
	}
}
