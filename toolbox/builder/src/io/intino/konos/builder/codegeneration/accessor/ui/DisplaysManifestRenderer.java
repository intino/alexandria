package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.DisplaysManifestTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.decorated.DecoratedDisplay;
import io.intino.konos.model.graph.ui.UIService;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class DisplaysManifestRenderer extends UIRenderer {
	private final UIService service;
	private final Set<String> renderedDisplays = new HashSet<>();

	protected DisplaysManifestRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() {
		FrameBuilder result = new FrameBuilder("manifest");
		Set<Display> displays = service.graph().rootDisplays().stream().filter(this::isGeneric).collect(toSet());
		Set<PassiveView> baseDisplays = baseDisplays(displays);

		baseDisplays.forEach(d -> renderDisplay(d, result));
		displays.stream().filter(d -> !renderedDisplays.contains(d.core$().id())).forEach(d -> renderDisplay(d, result));

		Commons.write(new File(accessorGen() + File.separator + "Displays.js").toPath(), setup(new DisplaysManifestTemplate()).render(result.toFrame()));
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
		builder.add("display", display(display));
		renderedDisplays.add(display.core$().id());
	}

	private <D extends PassiveView> boolean isGeneric(D element) {
		String className = element.getClass().getSimpleName();
		return className.equalsIgnoreCase("display") ||
			   className.equalsIgnoreCase("component") ||
			   className.equalsIgnoreCase("template") ||
			   className.equalsIgnoreCase("block") ||
			   className.equalsIgnoreCase("item") ||
			   className.equalsIgnoreCase("row");
	}

	private <D extends PassiveView> Frame display(D display) {
		FrameBuilder result = new FrameBuilder("display", typeOf(display));
		result.add("name", nameOf(display));
		result.add("directory", display.i$(DecoratedDisplay.class) ? "src" : "gen");
		return result.toFrame();
	}

}
