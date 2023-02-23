package io.intino.alexandria.resourcecleaner;

import java.lang.ref.Cleaner;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * <p>Represents an auto-disposable resource by the java.lang.ref.Cleaner. The resource will be released when the owner object becomes phantom-reachable.</p>
 * */
public class DisposableResource implements AutoCloseable {

	private static final Cleaner TheCleaner = Cleaner.create(r -> new Thread(r, "AlexandriaResourceCleaner"));

	public static Builder whenDestroyed(Object owner) {
		return new Builder(owner);
	}

	public static DisposableResource create(Object owner, Object resource) {
		return new Builder(owner).thenClose(resource);
	}

	public static DisposableResource create(Object owner, Object resource, Runnable callback) {
		return new Builder(owner).thenClose(resource, callback);
	}

	private final Cleaner.Cleanable cleanable;
	private final List<Runnable> closeHandlers = new LinkedList<>();

	private DisposableResource(Cleaner.Cleanable cleanable) {
		this.cleanable = cleanable;
	}

	public void addCloseHandler(Runnable closeHandler) {
		this.closeHandlers.add(requireNonNull(closeHandler));
	}

	@Override
	public void close() {
		cleanable.clean();
		closeHandlers.forEach(Runnable::run);
	}

	public static class Builder {

		private final Object owner;

		public Builder(Object owner) {
			this.owner = requireNonNull(owner);
		}

		public DisposableResource thenClose(Object resource) {
			requireNonNull(resource);
			if(resource == owner) throw new IllegalArgumentException("Owner and resource cannot be the same object!!");
			return new DisposableResource(TheCleaner.register(owner, () -> closeResource(resource)));
		}

		public DisposableResource thenClose(Object resource, Runnable callback) {
			requireNonNull(resource);
			if(resource == owner) throw new IllegalArgumentException("Owner and resource cannot be the same object!!");
			return new DisposableResource(TheCleaner.register(owner, () -> {closeResource(resource); callback.run();}));
		}
	}

	private static void closeResource(Object resource) {
		if(resource instanceof AutoCloseable) {
			try {
				((AutoCloseable) resource).close();
			} catch (Exception e) {
				throw new CleanException(e);
			}
		}
	}

	public static class CleanException extends RuntimeException {
		public CleanException(Throwable cause) {
			super(cause);
		}
	}
}
