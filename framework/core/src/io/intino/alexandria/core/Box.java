package io.intino.alexandria.core;

public abstract class Box {

	protected Box owner;

	public abstract void beforeStart();

	public abstract Box start();

	public abstract void afterStart();

	public abstract void beforeStop();

	public abstract void stop();

	public abstract void afterStop();

	public abstract Box put(Object object);

	public Box owner() {
		return owner;
	}


	public abstract BoxConfiguration configuration();
}
