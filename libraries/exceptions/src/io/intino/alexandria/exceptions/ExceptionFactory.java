package io.intino.alexandria.exceptions;

import java.util.Map;

public class ExceptionFactory {
	public static AlexandriaException from(int code, String message, Map<String, String> parameters) {
		if (code == 400) return new BadRequest(message, parameters);
		if (code == 401) return new Unauthorized(message, parameters);
		if (code == 403) return new Forbidden(message, parameters);
		if (code == 404) return new NotFound(message, parameters);
		if (code == 409) return new Conflict(message, parameters);
		if (code == 423) return new Locked(message, parameters);
		if (code == 501) return new NotImplemented(message, parameters);
		if (code == 503) return new ServiceUnavailable(message, parameters);
		return new InternalServerError(message, parameters);
	}
}
