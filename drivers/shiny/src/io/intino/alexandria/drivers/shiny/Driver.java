package io.intino.alexandria.drivers.shiny;

import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.drivers.shiny.functions.CleanQueryParam;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.proxy.ProxyAdapter;
import org.apache.commons.io.FileUtils;

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
	private static final String ShinyUrl = "http://10.13.13.37:3838";
//	private static final String ShinyScriptsFolder = "/src/shiny-server";
	private static final String ShinyScriptsFolder = "/tmp/shiny-server";

	public static final String Program = "Program";
	public static final String LocalUrlParameter = "LocalUrlParameter";

	@Override
	public boolean isPublished(String program) {
		if (!shinyScriptsFolder().exists()) return true;
		return shinyProgramDirectory(program).exists();
	}

	@Override
	public URL info(String program) {
		try {
			return new URL(String.format(ShinyUrl + "/%s", program));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public URL publish(Program program) {
		try {
			if (!shinyScriptsFolder().exists()) return new URL(String.format(ShinyUrl + "/%s", program.name()));
			shinyProgramDirectory(program.name()).mkdirs();
			publishScripts(program);
			publishResources(program);
			return new URL(String.format(ShinyUrl + "/%s", program));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public void unPublish(String program) {
		try {
			File file = shinyProgramDirectory(program);
			if (!file.exists()) return;
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public io.intino.alexandria.proxy.Proxy run(Map<String, Object> parameters) {
		String program = (String) parameters.get(Program);
		URL localUrl = (URL) parameters.get(LocalUrlParameter);
		return new io.intino.alexandria.proxy.Proxy(localUrl, info(program)).adapter(proxyAdapter());
	}

	private ProxyAdapter proxyAdapter() {
		return (localUrl, remoteUrl, param, value) -> {
			CleanQueryParam cleanQueryParam = new CleanQueryParam();
			return cleanQueryParam.execute(localUrl, param, value);
		};
	}

	private void publishScripts(Program program) {
		program.scripts().forEach(script -> {
			try {
				Path target = Paths.get(shinyProgramDirectory(program.name()) + File.separator + script.getFileName());
				String scriptContent = new String(Files.readAllBytes(script), StandardCharsets.UTF_8);
				replaceParameters(program, scriptContent);
				Files.write(target, scriptContent.getBytes());
			} catch (IOException e) {
				Logger.error(e);
			}
		});
	}

	private void publishResources(Program program) {
		program.resources().forEach(resource -> {
			try {
				Files.copy(resource, Paths.get(shinyProgramDirectory(program.name()) + File.separator + resource.getFileName()));
			} catch (IOException e) {
				Logger.error(e);
			}
		});
	}

	private void replaceParameters(Program program, String script) {
		program.parameters().forEach((key, value) -> replaceTag(script, key, value));
	}

	private String replaceTag(String content, String tag, String value) {
		return content.replaceAll(":" + tag + ":", value);
	}

	private File shinyProgramDirectory(String program) {
		return new File(shinyScriptsFolder() + File.separator + program);
	}

	private File shinyScriptsFolder() {
		return new File(ShinyScriptsFolder);
	}

}
