package io.intino.alexandria.ui.model;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MaterialIconExtractor {

	private static final String Path = "/Users/mcaballero/Downloads/material-design-icons-4.0.0/png";
	private static final String IconPath = "/materialicons/48dp/1x";
	private static final String DestinationPath = "./framework/ui/res/icons/mobile";

	public static void main(String[] args) {
		File root = new File(Path);
		new File(DestinationPath).mkdirs();
		List<File> iconsFamilyDir = Arrays.stream(Objects.requireNonNull(root.listFiles())).filter(File::isDirectory).collect(Collectors.toList());
		iconsFamilyDir.forEach(MaterialIconExtractor::processIconsFamily);
	}

	private static void processIconsFamily(File root) {
		List<File> iconsDir = Arrays.stream(Objects.requireNonNull(root.listFiles())).filter(File::isDirectory).collect(Collectors.toList());
		iconsDir.forEach(MaterialIconExtractor::processIcon);
	}

	private static void processIcon(File directory) {
		try {
			String iconName = firstUpperCase(snakeCaseToCamelCase(directory.getName()));
			File sourceDir = new File(directory, IconPath);
			if (!sourceDir.exists()) return;
			File content = Objects.requireNonNull(sourceDir.listFiles())[0];
			if (!content.exists()) return;
			Files.write(new File(DestinationPath, iconName + ".png").toPath(), Files.readAllBytes(content.toPath()));
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public static String snakeCaseToCamelCase(String string) {
		if (string.isEmpty()) {
			return string;
		} else {
			String result = "";
			String[] var2 = string.replace("_", "-").split("-");
			int var3 = var2.length;

			for(int var4 = 0; var4 < var3; ++var4) {
				String part = var2[var4];
				result = result + String.valueOf(Character.toUpperCase(part.charAt(0))) + part.substring(1);
			}

			return result;
		}
	}

	private static String firstUpperCase(String content) {
		return content.substring(0, 1).toUpperCase() + content.substring(1);
	}

}
