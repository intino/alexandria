package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.Axis;

import java.io.*;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;

public class CategoricalAxisRenderer {

    public static final int LABEL_INDEX = 2;
    public static final int MAX_EMBEDDED_COMPONENTS = 100;

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
                firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString()),
                customize(new CategoricalAxisTemplate()).render(fb.toFrame()));
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

    private void addLabel(FrameBuilder fb, Axis.Categorical axis) {
        if (axis.includeLabel() != null)
            fb.add("include", new FrameBuilder("include").add("name", "label").add("index", LABEL_INDEX));
    }

    private void addIncludes(FrameBuilder fb, Axis.Categorical axis, List<Axis> includes) {
        final int offset = offset(axis);
        for (int i = 0; i < includes.size(); i++) {
            fb.add("include", new FrameBuilder("include")
                    .add("name", includes.get(i).name$())
                    .add("index", i + offset));
        }
    }

    private void loadComponents(FrameBuilder fb, Axis.Categorical axis, List<Axis> includes) {
        String resource = axisResource(axis.tsv().getPath());
        List<ComponentInfo> components = loadFromResource(resource);
        final boolean embedded = isSmallEnough(components);

        if(embedded) createEmbeddedComponents(fb, axis, includes, components);

        FrameBuilder componentsFB = new FrameBuilder("components");

        componentsFB.add("embedded", embedded);
        if(embedded)
            addEmbeddedComponentsToArray(componentsFB, axis, components);
        else
            componentsFB.add("resource", resource);

        addLabel(componentsFB, axis);
        addIncludes(componentsFB, axis, includes);

        fb.add("components", componentsFB);
    }

    private void addEmbeddedComponentsToArray(FrameBuilder fb, Axis.Categorical axis, List<ComponentInfo> components) {
        final boolean hasLabel = axis.includeLabel() != null;
        for(ComponentInfo component : components) {
            final String name = hasLabel ? component.label() : component.id();
            fb.add("component", asFieldName(name));
        }
    }

    private void createEmbeddedComponents(FrameBuilder fb, Axis.Categorical axis, List<Axis> includes, List<ComponentInfo> components) {
        for(ComponentInfo component : components) {
            FrameBuilder compFB = new FrameBuilder("component");
            final boolean hasLabel = axis.includeLabel() != null;
            final String name = hasLabel ? component.label() : component.id();
            compFB.add("name", asFieldName(name));
            if(hasLabel) compFB.add("label", component.label());
            compFB.add("index", component.index());
            compFB.add("id", component.id());
            addIncludes(compFB, axis, includes);
            fb.add("component", compFB);
        }
    }

    private String asFieldName(String id) {
        id = id.replaceAll("\\s+", "_").replace('-', '_');
        return Character.isDigit(id.charAt(0)) ? "_" + id : id;
    }

    private boolean isSmallEnough(List<ComponentInfo> components) {
        return components.size() <= MAX_EMBEDDED_COMPONENTS;
    }

    private List<ComponentInfo> loadFromResource(String resource) {
        final String fullResourcePath = new File(res, resource).getAbsolutePath();
        try(BufferedReader reader = new BufferedReader(new FileReader(fullResourcePath))) {
            return reader.lines().map(line -> line.split("\t")).map(ComponentInfo::new).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private String axisResource(String resource) {
        Path res = context.res(Target.Owner).toPath();
        return res.relativize(new File(resource).toPath().toAbsolutePath()).toFile().getPath().replace("\\", "/");
    }

    private int offset(Axis.Categorical axis) {
        return axis.includeLabel() != null ? 3 : 2;
    }

    private String getJavaFilename(Axis.Categorical axis) {
        return firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString());
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
