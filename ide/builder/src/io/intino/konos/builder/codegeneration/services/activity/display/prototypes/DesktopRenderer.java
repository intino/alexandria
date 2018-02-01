package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Activity;
import io.intino.konos.model.graph.Desktop;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class DesktopRenderer extends PrototypeRenderer {
	private final Project project;

	public DesktopRenderer(Project project, Desktop desktop, File src, File gen, String packageName, String boxName) {
		super(desktop, boxName, packageName, src, gen);
		this.project = project;
	}

	public void render() {
		Frame frame = createFrame();
		writeSrc(frame);
		writeAbstract(frame.addTypes("gen"));
	}

	protected Frame createFrame() {
		final Desktop desktop = this.display.a$(Desktop.class);
		final Frame frame = super.createFrame();
		frame.addSlot("title", desktop.title());
		frame.addSlot("subtitle", desktop.subTitle());
		if (desktop.logoPath() != null && !desktop.logoPath().isEmpty()) frame.addSlot("logo", desktop.logoPath());
		if (desktop.faviconPath() != null && !desktop.faviconPath().isEmpty()) frame.addSlot("favicon", desktop.faviconPath());
		Activity used = findOwnerActivity();
		if (used != null && used.authenticated() != null) frame.addSlot("activity", used.name$());
		if (desktop.layout() != null) frame.addSlot("layout", desktop.layout().name$());
		return frame;
	}

	private Activity findOwnerActivity() {
		return this.display.graph().activityList().stream().filter(activity -> activity.userHome() != null && this.display.equals(activity.userHome().uses())).findFirst().orElse(null);
	}

	protected Template template() {
		return customize(DesktopTemplate.create());
	}

	@Override
	protected Template srcTemplate() {
		return template();
	}

}
