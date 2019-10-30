package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.DisplaysManifestTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.Service;

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

	protected DisplaysManifestRenderer(Settings settings, Service.UI service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() {
		Set<Display> displays = service.graph().rootDisplays().stream().filter(this::isBaseType).collect(toSet());
		Set<PassiveView> baseDisplays = baseDisplays(displays);
		renderDisplays(displays, baseDisplays);
		renderedDisplays.clear();
		renderAccessibleDisplays(displays, baseDisplays);
	}

	private void renderDisplays(Set<Display> displays, Set<PassiveView> baseDisplays) {
		FrameBuilder result = new FrameBuilder("manifest");

		baseDisplays.forEach(d -> renderDisplay(d, result));
		displays.stream().filter(d -> !renderedDisplays.contains(d.core$().id())).forEach(d -> renderDisplay(d, result));

		Commons.write(new File(gen() + File.separator + "Displays.js").toPath(), setup(new DisplaysManifestTemplate()).render(result.toFrame()));
	}

	private void renderAccessibleDisplays(Set<Display> displays, Set<PassiveView> baseDisplays) {
		FrameBuilder result = new FrameBuilder("manifest");

		baseDisplays.stream().filter(d -> d.i$(Display.Accessible.class)).forEach(d -> renderDisplay(d, result, true));
		displays.stream().filter(d -> !renderedDisplays.contains(d.core$().id()) && d.isAccessible()).forEach(d -> renderDisplay(d, result, true));

		Commons.write(new File(gen() + File.separator + "AccessibleDisplays.js").toPath(), setup(new DisplaysManifestTemplate()).render(result.toFrame()));
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

	private <D extends PassiveView> void renderDisplay(D display, FrameBuilder builder, boolean accessible) {
		builder.add("display", display(display, accessible));
		renderedDisplays.add(display.core$().id());
	}

	private <D extends PassiveView> Frame display(D display, boolean accessible) {
		FrameBuilder result = new FrameBuilder("display", typeOf(display));

		result.add("name", nameOf(display));
		result.add("directory", ElementHelper.isRoot(display) ? "src" : "gen");

		if (accessible) {
			result.add("accessible");
			result.add("accessible", new FrameBuilder("accessible"));
		}

		return result.toFrame();
	}

}
