package io.intino.konos.builder.codegeneration.services.ui.display.desktop;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.ui.display.panel.PanelRenderer;
import io.intino.konos.model.graph.Panel;
import io.intino.konos.model.graph.desktop.DesktopPanel;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;

public class DesktopRenderer extends PanelRenderer {

	public DesktopRenderer(Project project, DesktopPanel desktop, File src, File gen, String packageName, String boxName) {
		super(project, desktop.a$(Panel.class), src, gen, packageName, boxName);
	}

	protected Frame createFrame() {
		final DesktopPanel desktop = this.display.a$(Panel.class).asDesktop();
		final Frame frame = super.createFrame();
		frame.addTypes("desktop");
		frame.addSlot("title", desktop.title());
		frame.addSlot("subtitle", desktop.subTitle());
		frame.addSlot("layout", desktop.layout().name());
		if (desktop.logoPath() != null && !desktop.logoPath().isEmpty()) frame.addSlot("logo", desktop.logoPath());
		if (desktop.faviconPath() != null && !desktop.faviconPath().isEmpty()) frame.addSlot("favicon", desktop.faviconPath());
		UIService service = findOwnerUIService();
		if (service != null && service.authentication() != null) frame.addSlot("uiService", service.name$());
		return frame;
	}

	private UIService findOwnerUIService() {
		return this.display.graph().uIServiceList().stream().filter(s -> s.userHome() != null && this.display.equals(s.userHome().uses())).findFirst().orElse(null);
	}

}
