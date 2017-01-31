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

	public static final Icon ICON_16 = RETINA ? scale(IconLoader.getIcon("/icons/konos/icon_retina.png")) : IconLoader.getIcon("/icons/konos/icon_16.png");
}
