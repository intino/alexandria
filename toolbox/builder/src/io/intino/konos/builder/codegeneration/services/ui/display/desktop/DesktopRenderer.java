package io.intino.konos.builder.codegeneration.services.ui.display.desktop;

import com.intellij.openapi.project.Project;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.display.panel.PanelRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.Panel;
import io.intino.konos.model.graph.desktop.DesktopPanel;
import io.intino.konos.model.graph.ui.UIService;

import static io.intino.konos.model.graph.KonosGraph.componentFor;

public class DesktopRenderer extends PanelRenderer {

	public DesktopRenderer(Project project, DesktopPanel desktop, String packageName, String boxName) {
		super(project, desktop.a$(Panel.class), packageName, boxName);
	}

	@Override
	public FrameBuilder frameBuilder() {
		final DesktopPanel desktop = display().a$(Panel.class).asDesktop();
		final FrameBuilder frame = super.frameBuilder();
		frame.add("desktop");
		frame.add("title", desktop.title());
		frame.add("subtitle", desktop.subTitle());
		frame.add("layout", desktop.layout().name());
		if (desktop.logoPath() != null && !desktop.logoPath().isEmpty()) frame.add("logo", desktop.logoPath());
		if (desktop.faviconPath() != null && !desktop.faviconPath().isEmpty()) frame.add("favicon", desktop.faviconPath());
		UIService service = findOwnerUIService();
		if (service != null && service.authentication() != null)
			frame.add("authentication", new FrameBuilder(isCustom(service.authentication().by()) ? "custom" : "standard").add("value", service.authentication().by()));
		return frame;
	}

	private boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}

	private UIService findOwnerUIService() {
		Display display = display();
		return display.graph().uIServiceList().stream().filter(s -> s.userHome() != null && display.equals(componentFor(s.userHome()))).findFirst().orElse(null);
	}

}
