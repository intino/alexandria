package io.intino.alexandria.drivers.utils;

public class SystemOS {

	public static final boolean isWindows()  {
    return System.getProperty("os.name").startsWith("Windows");
 }	

}
