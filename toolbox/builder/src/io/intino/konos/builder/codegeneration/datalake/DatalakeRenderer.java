package io.intino.konos.builder.codegeneration.datalake;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Datalake;
import io.intino.konos.model.graph.Datalake.Tank;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.remote.RemoteDatalake;

import java.io.File;

public class DatalakeRenderer {
	private final Datalake datalake;
	private final File gen;
	private final String packageName;
	private final String boxName;

	public DatalakeRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.datalake = graph.datalake();
	}

	public void execute() {
		FrameBuilder builder = new FrameBuilder("tanks").
				add("package", packageName).
				add("box", boxName).
				add("tank", datalake.tankList().stream().map(this::frameOf).toArray(Frame[]::new));
		if (datalake.isRemote()) {
			RemoteDatalake remote = datalake.asRemote();
			FrameBuilder remoteFrame = new FrameBuilder("remote")
					.add("url", new FrameBuilder(isCustom(remote.url()) ? "custom" : "regular").add("value", remote.url()))
					.add("user", new FrameBuilder(isCustom(remote.user()) ? "custom" : "regular").add("value", remote.url()))
					.add("password", new FrameBuilder(isCustom(remote.password()) ? "custom" : "regular").add("value", remote.url()))
					.add("clientId", new FrameBuilder(isCustom(remote.clientID()) ? "custom" : "regular").add("value", remote.url()));
			builder.add("remote", remoteFrame);
		}
		Commons.writeFrame(new File(gen, "datalake"), "Datalake", template().render(builder.toFrame()));
	}

	private Frame frameOf(Tank tank) {
		final FrameBuilder builder = new FrameBuilder("tank", tank.isEvent() ? "event" : "set").
				add("name", tank.name$()).
				add("fullname", tank.fullName());
		return builder.toFrame();
	}

	private boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}

	private Template template() {
		return Formatters.customize(new DatalakeTemplate()).add("customParameter", value -> value.toString().substring(1, value.toString().length() - 1));
	}
}
