package io.intino.konos.builder;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manifest {
	public List<Action> actions;
	public Map<String, String> dependencies;

	public Manifest() {
		actions = new ArrayList<>();
		dependencies = new HashMap<>();
	}

	public static Manifest load() {
		ClassLoader classLoader = Manifest.class.getClassLoader();
		InputStream stream = classLoader.getResourceAsStream("manifest.json");
		return new Gson().fromJson(new InputStreamReader(stream), Manifest.class);
	}

	public static class Action {
		public String id;
		public String aClass;
		public String groupId;
		public String shortcut;
		public String anchor;
		public String relativeToAction;
	}

}
