package io.intino.alexandria.drivers.shiny;

import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.drivers.shiny.functions.CleanQueryParam;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.proxy.ProxyAdapter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Driver implements io.intino.alexandria.drivers.Driver<URL, io.intino.alexandria.proxy.Proxy> {
	private final String shinyUrl;
	private static final String ShinyScriptsFolder = "/srv/shiny-server";

	public static final String LocalUrlParameter = "LocalUrlParameter";

	public Driver(String shinyUrl) {
		this.shinyUrl = shinyUrl;
	}

	@Override
	public URL info(String program) {
		try {
			return new URL(String.format(shinyUrl + "/%s", program));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public boolean isPublished(String program) {
		if (!shinyScriptsFolder().exists()) return true;
		return new File(shinyProgramDirectory(program) + "/ui.R").exists() && new File(shinyProgramDirectory(program) + "/server.R").exists();
	}

	@Override
	public URL publish(Program program) {
		try {
			String programUrl = shinyUrl + "/" + program.name();
			if (!shinyScriptsFolder().exists()) return new URL(programUrl);
			update(program);
			return new URL(programUrl);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public void update(Program program) {
		if (!shinyScriptsFolder().exists()) return;
		shinyProgramDirectory(program.name()).mkdirs();
		publishScripts(program);
		publishResources(program);
	}

	@Override
	public void unPublish(String program) {
		File uiFile = new File(shinyProgramDirectory(program) + "/ui.R");
		if (uiFile.exists()) uiFile.delete();

		File serverFile = new File(shinyProgramDirectory(program) + "/server.R");
		if (serverFile.exists()) serverFile.delete();
	}

	@Override
	public io.intino.alexandria.proxy.Proxy run(Program program) {
		URL localUrl = (URL) program.parameters().get(LocalUrlParameter);
		return new io.intino.alexandria.proxy.Proxy(localUrl, info(program.name())).adapter(proxyAdapter());
	}

	private ProxyAdapter proxyAdapter() {
		return new ProxyAdapter() {
			@Override
			public String adaptParameter(URL localUrl, URL remoteUrl, String param, String value) {
				CleanQueryParam cleanQueryParam = new CleanQueryParam();
				return cleanQueryParam.execute(localUrl, param, value);
			}

			@Override
			public String adaptContent(String content) {
				return content;
			}
		};
	}

	private void publishScripts(Program program) {
		program.scripts().forEach(script -> {
			try {
				Path target = Paths.get(shinyProgramDirectory(program.name()) + File.separator + script.name());
				String scriptContent = new String(IOUtils.toByteArray(script.content()), StandardCharsets.UTF_8);
				scriptContent = replaceParameters(program, scriptContent);
				Files.write(target, scriptContent.getBytes());
			} catch (IOException e) {
				Logger.error(e);
			}
		});
	}

	private void publishResources(Program program) {
		program.resources().forEach(resource -> {
			try {
				File file = new File(shinyProgramDirectory(program.name()) + File.separator + resource.name());
				if (file.exists()) file.delete();
				Files.copy(resource.content(), Paths.get(shinyProgramDirectory(program.name()) + File.separator + resource.name()));
			} catch (IOException e) {
				Logger.error(e);
			}
		});
	}

	private String replaceParameters(Program program, String script) {
		for (Map.Entry<String, Object> entry : program.parameters().entrySet()) {
			Object value = entry.getValue();
			script = replaceTag(script, entry.getKey(), value != null ? String.valueOf(value) : "");
		}
		script = script.replaceAll("###", "");
		return script;
	}

	private String replaceTag(String content, String tag, String value) {
		content = content.replaceAll(":" + tag + ":", value);
		return content;
	}

	private File shinyProgramDirectory(String program) {
		return new File(shinyScriptsFolder() + File.separator + program);
	}

	private File shinyScriptsFolder() {
		return new File(ShinyScriptsFolder);
	}

}
