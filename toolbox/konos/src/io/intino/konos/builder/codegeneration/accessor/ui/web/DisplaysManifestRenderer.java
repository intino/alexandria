package io.intino.konos.builder.codegeneration.accessor.ui.web;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.accessor.ui.web.templates.DisplaysManifestTemplate;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.dsl.Display;
import io.intino.konos.dsl.PassiveView;
import io.intino.konos.dsl.Service;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class DisplaysManifestRenderer extends UIRenderer {
	private final Service.UI service;
	private final Set<String> renderedDisplays = new HashSet<>();

	protected DisplaysManifestRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext);
		this.service = service;
	}

	@Override
	public void render() {
		Set<Display> displays = service.graph().rootDisplays(context.graphName()).stream().filter(this::isBaseType).collect(toSet());
		Set<PassiveView> baseDisplays = baseDisplays(displays);
		renderDisplays(displays, baseDisplays);
		renderedDisplays.clear();
		renderExposedDisplays(displays, baseDisplays);
	}

	private void renderDisplays(Set<Display> displays, Set<PassiveView> baseDisplays) {
		FrameBuilder result = new FrameBuilder("manifest");

		baseDisplays.forEach(d -> renderDisplay(d, result));
		displays.stream().filter(d -> !renderedDisplays.contains(d.core$().id())).forEach(d -> renderDisplay(d, result));

		Commons.write(new File(gen(Target.Accessor) + File.separator + "Displays.js").toPath(), new DisplaysManifestTemplate().render(result.toFrame(), Formatters.all));
	}

	private void renderExposedDisplays(Set<Display> displays, Set<PassiveView> baseDisplays) {
		FrameBuilder result = new FrameBuilder("manifest");

		baseDisplays.stream().filter(d -> d.i$(Display.Exposed.class)).forEach(d -> renderDisplay(d, result, true));
		displays.stream().filter(d -> !renderedDisplays.contains(d.core$().id()) && d.isExposed()).forEach(d -> renderDisplay(d, result, true));

		Commons.write(new File(gen(Target.Accessor) + File.separator + "ExposedDisplays.js").toPath(), new DisplaysManifestTemplate().render(result.toFrame(), Formatters.all));
	}

	private Set<PassiveView> baseDisplays(Set<Display> displays) {
		return displays.stream().filter(Display::isExtensionOf).map(this::baseDisplays).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	private Set<PassiveView> baseDisplays(PassiveView view) {
		Set<PassiveView> result = new LinkedHashSet<>();
		if (view.isExtensionOf()) {
			PassiveView passiveView = view.asExtensionOf().parentView();
			result.addAll(baseDisplays(passiveView));
		}
		result.add(view);
		return result;
	}

	private <D extends PassiveView> void renderDisplay(D display, FrameBuilder builder) {
		renderDisplay(display, builder, false);
	}

	private <D extends PassiveView> void renderDisplay(D display, FrameBuilder builder, boolean exposed) {
		builder.add("display", display(display, exposed));
		renderedDisplays.add(display.core$().id());
	}

	private <D extends PassiveView> Frame display(D display, boolean exposed) {
		FrameBuilder result = new FrameBuilder("display", typeOf(display));

		result.add("name", nameOf(display));
		result.add("directory", ElementHelper.isRoot(display) ? "src" : "gen");

		if (exposed) {
			result.add("exposed");
			result.add("exposed", new FrameBuilder("exposed"));
		}

		return result.toFrame();
	}

}
