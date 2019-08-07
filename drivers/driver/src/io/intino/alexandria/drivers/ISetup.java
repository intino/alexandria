package io.intino.alexandria.drivers;

public interface ISetup {
	boolean isInstalled();
	void install();
	void uninstall();
}
