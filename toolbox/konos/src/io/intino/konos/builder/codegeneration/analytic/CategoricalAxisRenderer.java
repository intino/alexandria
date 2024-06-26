package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.formatters.StringFormatters;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Axis;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class CategoricalAxisRenderer {
	public static final int LABEL_INDEX = 2;
	public static final int MAX_EMBEDDED_COMPONENTS = 100;
	private static final String VARIABLE_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

	private final CompilationContext context;
	private final File gen;
	private final File res;

	public CategoricalAxisRenderer(CompilationContext context, File gen, File res) {
		this.context = context;
		this.gen = gen;
		this.res = res;
	}

	public void render(Axis.Categorical axis) {
		FrameBuilder fb = new FrameBuilder("axis");
		addBasicAxisInfo(fb, axis);
		handleIncludes(fb, axis);
		writeAxisFrame(fb, axis);
		addToCompiledFiles(axis);
	}

	private void addBasicAxisInfo(FrameBuilder fb, Axis.Categorical axis) {
		fb.add("package", context.packageName()).add("name", axis.name$()).add("label", axis.label());
		if (axis.asAxis().isDynamic())
			fb.add("dynamic", ";");
	}

	private void writeAxisFrame(FrameBuilder fb, Axis.Categorical axis) {
		writeFrame(new File(gen, "axes"),
				StringFormatters.pascalCase().format(axis.name$()).toString(),
				new CategoricalAxisTemplate().render(fb.toFrame(), Formatters.all));
	}

	private void addToCompiledFiles(Axis.Categorical axis) {
		context.compiledFiles().add(new OutputItem(
				context.sourceFileOf(axis),
				javaFile(new File(gen, "axes"),
						getJavaFilename(axis)).getAbsolutePath()));
	}

	private void handleIncludes(FrameBuilder fb, Axis.Categorical axis) {
		addLabel(fb, axis);
		List<Axis> includes = axis.include() == null ? Collections.emptyList() : axis.include().axes();
		loadComponents(fb, axis, includes);
		addIncludes(fb, axis, includes);
	}

	private void loadComponents(FrameBuilder fb, Axis.Categorical axis, List<Axis> includes) {
		String resource = "/" + axisResource(axis.tsv().getPath());
		List<ComponentInfo> components = loadFromResource(resource);
		final boolean embedded = isSmallEnough(components);

		if (embedded) createEmbeddedComponents(fb, axis, includes, components);

		FrameBuilder componentsFB = new FrameBuilder("components");
		componentsFB.add("name", axis.name$());

		addLabel(componentsFB, axis);

		componentsFB.add("embedded", embedded);
		if (embedded) {
			addEmbeddedComponentsToArray(componentsFB, axis, components);
		} else {
			componentsFB.add("resource", resource);
			addIncludes(componentsFB, axis, includes);
		}

		fb.add("components", componentsFB);
	}

	private void addLabel(FrameBuilder fb, Axis.Categorical axis) {
		if (axis.includeLabel() != null)
			fb.add("include", new FrameBuilder("include")
					.add("axis", axis.name$())
					.add("name", "label")
					.add("label", "label")
					.add("index", LABEL_INDEX));
	}

	private void addIncludes(FrameBuilder fb, Axis.Categorical axis, List<Axis> includes) {
		final int offset = offset(axis);
		for (int i = 0; i < includes.size(); i++) {
			fb.add("include", new FrameBuilder("include")
					.add("axis", axis.name$())
					.add("name", includes.get(i).name$())
					.add("type", includes.get(i).isCategorical() ? "categorical" : "continuous")
					.add("label", asFieldName(includes.get(i).label()))
					.add("index", i + offset));
		}
	}

	private void addIncludes(ComponentInfo component, FrameBuilder fb, Axis.Categorical axis, List<Axis> includes) {
		final boolean hasLabel = axis.includeLabel() != null;
		final int offset = offset(axis);
		for (int i = 0; i < includes.size(); i++) {
			final String include = component.field(i + (hasLabel ? 3 : 2));
			fb.add("include", new FrameBuilder("include")
					.add("name", includes.get(i).name$())
					.add("label", asFieldName(includes.get(i).label()))
					.add("id", include)
					.add("type", includes.get(i).isCategorical() ? "categorical" : "continuous")
					.add("index", i + offset));
		}
	}

	private void addEmbeddedComponentsToArray(FrameBuilder fb, Axis.Categorical axis, List<ComponentInfo> components) {
		final boolean useLabel = shouldUseLabel(axis, components);
		for (ComponentInfo component : components) {
			final String name = useLabel ? component.label() : component.id();
			fb.add("component", asFieldName(name));
		}
	}

	private void createEmbeddedComponents(FrameBuilder fb, Axis.Categorical axis, List<Axis> includes, List<ComponentInfo> components) {
		final boolean useLabel = shouldUseLabel(axis, components);
		for (ComponentInfo component : components) {
			FrameBuilder compFB = new FrameBuilder("component");
			final String name = useLabel ? component.label() : component.id();
			compFB.add("name", asFieldName(name));
			if (axis.includeLabel() != null) compFB.add("label", component.label());
			compFB.add("index", component.index());
			compFB.add("id", component.id());
			addIncludes(component, compFB, axis, includes);
			fb.add("component", compFB);
		}
	}

	private boolean shouldUseLabel(Axis.Categorical axis, List<ComponentInfo> components) {
		return axis.includeLabel() != null && axis.includeLabel().isName() && checkLabelNames(components);
	}

	private boolean checkLabelNames(List<ComponentInfo> components) {
		for (ComponentInfo component : components) {
			if (!asFieldName(component.label()).matches(VARIABLE_PATTERN)) return false;
		}
		return true;
	}

	private String asFieldName(String name) {
		name = name.replaceAll("\\s+", "_").replace('-', '_');
		name = StringUtils.stripAccents(name);
		return Character.isDigit(name.charAt(0)) ? "_" + name : name;
	}

	private boolean isSmallEnough(List<ComponentInfo> components) {
		return components.size() <= MAX_EMBEDDED_COMPONENTS;
	}

	private List<ComponentInfo> loadFromResource(String resource) {
		final String fullResourcePath = new File(res, resource).getAbsolutePath();
		try (BufferedReader reader = new BufferedReader(new FileReader(fullResourcePath))) {
			return reader.lines().map(line -> line.split("\t")).map(ComponentInfo::new).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private String axisResource(String resource) {
		Path res = context.res(Target.Service).toPath();
		return res.relativize(new File(resource).toPath().toAbsolutePath()).toFile().getPath().replace("\\", "/");
	}

	private int offset(Axis.Categorical axis) {
		return axis.includeLabel() != null ? 3 : 2;
	}

	private String getJavaFilename(Axis.Categorical axis) {
		return StringFormatters.pascalCase().format(axis.name$()).toString();
	}


	private static class ComponentInfo {

		private final String[] fields;

		public ComponentInfo(String[] fields) {
			this.fields = fields;
		}

		public int index() {
			return Integer.parseInt(field(0));
		}

		public String id() {
			return field(1);
		}

		public String field(int index) {
			return fields[index];
		}

		public String label() {
			return fields[LABEL_INDEX];
		}
	}
}