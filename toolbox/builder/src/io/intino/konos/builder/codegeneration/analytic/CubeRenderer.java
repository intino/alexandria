package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.facts.FactRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Axis;
import io.intino.konos.model.graph.Cube;

import java.io.File;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.*;

public class CubeRenderer {

    private final File src;
    private final File gen;
    private final FactRenderer factRenderer;
    private final CompilationContext context;

    public CubeRenderer(File src, File gen, CompilationContext context, FactRenderer factRenderer) {
        this.src = src;
        this.gen = gen;
        this.context = context;
        this.factRenderer = factRenderer;
    }

    public void render(Cube cube, FrameBuilder fb) {
        if(cube.isVirtual())
            renderVirtualCube(cube.asVirtual(), fb);
        else
            renderCube(cube, fb);
    }

    private void renderCube(Cube cube, FrameBuilder fb) {
        addDimensionsAndIndicators(cube, null, fb);
        if (cube.splitted() != null) addSplit(cube, fb);
        addIndex(fb, cube.index());
        factRenderer.render(cube, fb);
        write(cube, fb);
    }

    private void renderVirtualCube(Cube.Virtual virtualCube, FrameBuilder fb) {
        for (Cube reference : new Cube[]{virtualCube.main(), virtualCube.join()}) {
            addDimensionsAndIndicators(reference, virtualCube.asCube(), fb);
            factRenderer.render(reference, fb);
        }
        addIndex(fb, virtualCube.index());
        fb.add("mainCube", virtualCube.main().name$());
        fb.add("joinCube", virtualCube.join().name$());
        fb.add("cube", new String[]{virtualCube.main().name$(), virtualCube.join().name$()});
        if (virtualCube.main().splitted() != null) addSplit(virtualCube.main(), fb);
        write(virtualCube.asCube(), fb);
    }

    private void write(Cube cube, FrameBuilder fb) {
        Template template = customize(template(cube));

        writeFrame(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$()), template.render(fb.toFrame()));
        context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube),
                javaFile(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$())).getAbsolutePath()));

        if (alreadyRendered(new File(src, "cubes"), cube.name$())) return;

        writeFrame(new File(src, "cubes"), cube.name$(), template.render(fb.add("src").toFrame()));
        context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube),
                javaFile(new File(src, "cubes"), firstUpperCase(cube.name$())).getAbsolutePath()));
    }

    private void addDimensionsAndIndicators(Cube cube, Cube sourceCube, FrameBuilder fb) {
        cube.dimensionList().forEach(selector -> fb.add("dimension", dimensionFrame(cube, sourceCube, selector)));
        cube.indicatorList().forEach(indicator -> fb.add("indicator", indicatorFrame(cube, sourceCube, indicator)));
        addCustomFilters(cube, sourceCube, fb);
    }

    private void addCustomFilters(Cube cube, Cube sourceCube, FrameBuilder fb) {
        for (Cube.CustomFilter customFilter : cube.customFilterList()) {
            fb.add("customFilter", new FrameBuilder("customFilter")
                .add("cube", sourceCube != null ? sourceCube.name$() : cube.name$())
                .add("name", customFilter.name$()));
        }
    }

    private FrameBuilder dimensionFrame(Cube cube, Cube sourceCube, Cube.Dimension dimension) {
        FrameBuilder fb = new FrameBuilder("dimension", dimension.axis().core$().is(Axis.Categorical.class) ? "categorical" : "continuous").
                add("cube", sourceCube != null ? sourceCube.name$() : cube.name$()).
                add("name", dimension.name$()).
                add("source", dimension.attribute().name$()).
                add("axis", dimension.axis().name$());

        if (dimension.axis().i$(Axis.Categorical.class)) {
            fb.add("type", dimension.axis().name$());
            if (!dimension.attribute().asCategory().axis().equals(dimension.axis()))
                fb.add("child", dimension.axis().name$());
        }

        return fb;
    }

    private FrameBuilder indicatorFrame(Cube cube, Cube sourceCube, Cube.Indicator indicator) {
        return new FrameBuilder("indicator").add(indicator.isAverage() ? "average" : "sum").
                add("cube", sourceCube != null ? sourceCube.name$() : cube.name$()).
                add("name", indicator.name$()).
                add("label", indicator.label()).
                add("index", new FrameBuilder((sourceCube == null && cube.index() != null) || (sourceCube != null && sourceCube.index() != null) ? "index" : "total")).
                add("source", indicator.source().name$()).
                add("unit", indicator.unit());
    }

    private void addSplit(Cube cube, FrameBuilder fb) {
        fb.add("split", new FrameBuilder("split")
                .add("name", cube.splitted().name$())
                .add("value", cube.splitted().splits().toArray(new String[0])));
    }

    private Template template(Cube cube) {
        return cube.isVirtual() ? new VirtualCubeTemplate() : new CubeWithGettersTemplate();
    }

    private boolean alreadyRendered(File destination, String name) {
        return Commons.javaFile(destination, name).exists();
    }

    private void addIndex(FrameBuilder fb, Cube.Index index) {
        fb.add("index", new FrameBuilder("index", index != null ? "normal" : "total").add("value", ""));
    }
}
