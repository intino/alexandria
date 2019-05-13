package io.intino.konos.builder;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.IconUtil;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;

public class KonosIcons {

	private static final boolean RETINA = UIUtil.isRetina();

	private static Icon scale(Icon icon) {
		return IconUtil.scale(icon, 0.5);
	}

	public static final Icon ICON_16 = RETINA ? scale(IconLoader.getIcon("/icons/files/box-32.png")) : IconLoader.getIcon("/icons/files/box-16.png");
	public static final Icon GENERATE_16 = RETINA ? scale(IconLoader.getIcon("/icons/operations/generate-32.png")) : IconLoader.getIcon("/icons/operations/generate-16.png");
	public static final Icon PUBLISH_16 = RETINA ? scale(IconLoader.getIcon("/icons/operations/publish-32.png")) : IconLoader.getIcon("/icons/operations/publish-16.png");
}
