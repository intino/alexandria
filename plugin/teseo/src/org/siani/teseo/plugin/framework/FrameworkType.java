package org.siani.teseo.plugin.framework;

import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;
import org.siani.teseo.plugin.TeseoIcons;

import javax.swing.*;

public class FrameworkType extends FrameworkTypeEx {

	protected FrameworkType(@NotNull String id) {
		super(id);
	}


	public static FrameworkType getFrameworkType() {
		return EP_NAME.findExtension(FrameworkType.class);
	}

	@NotNull
	@Override
	public FrameworkSupportInModuleProvider createProvider() {
		return new ForrestSupportProvider();
	}

	@NotNull
	@Override
	public String getPresentableName() {
		return "Forrest Framework";
	}

	@NotNull
	@Override
	public Icon getIcon() {
		return TeseoIcons.ICON_16;
	}

	private class ForrestSupportProvider extends FrameworkSupportInModuleProvider {
		@NotNull
		@Override
		public FrameworkTypeEx getFrameworkType() {
			return FrameworkType.getFrameworkType();
		}

		@NotNull
		@Override
		public FrameworkSupportInModuleConfigurable createConfigurable(@NotNull FrameworkSupportModel model) {
			return null;
		}

		@Override
		public boolean isEnabledForModuleType(@NotNull ModuleType moduleType) {
			return moduleType instanceof JavaModuleType;
		}
	}
}
