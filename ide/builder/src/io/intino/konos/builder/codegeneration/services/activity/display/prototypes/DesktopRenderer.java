package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import com.intellij.util.Base64;
import io.intino.konos.model.graph.Desktop;
import org.apache.commons.io.IOUtils;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
		if (desktop.logo() != null) frame.addSlot("logo", toBase64(desktop.logo()));
		if (desktop.favicon() != null) frame.addSlot("favicon", toBase64(desktop.favicon()));
		if (desktop.layout() != null) frame.addSlot("layout", desktop.layout().name$());
		return frame;
	}

	protected Template template() {
		return customize(DesktopTemplate.create());
	}

	private static String toBase64(URL resource) {
		try {
			return "data:image/png;base64," + Base64.encode(toByteArray(resource.openStream()));
		} catch (IOException e) {
			return "";
		}
	}

	private static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		IOUtils.copy(input, output);
		return output.toByteArray();
	}

}
