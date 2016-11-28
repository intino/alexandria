import com.moandjiezana.toml.Toml;

import java.io.InputStream;
import java.util.List;

public class Builder {
	String name;
	String version;
	String changeNotes;

	List<Action> actions;
	List<Group> groups;

	static Builder from(InputStream manifest) {
		final Toml read = new Toml().read(manifest);
		System.out.println(read.getString("name"));
		return manifest == null ? null : read.to(Builder.class);
	}

	static class Action {
		String id;
		String aClass;
		String groupId;
		String anchor;
	}

	private class Group {
		String id;
		String popup;
		String text;
		String aclass;
		String groupId;

	}
}