package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplaysManifestTemplate extends Template {

	protected DisplaysManifestTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplaysManifestTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "manifest"))).add(mark("display", "import").multiple("\n")).add(literal("\nexport { ")).add(mark("display", "export").multiple(",")).add(literal(" }")),
			rule().add((condition("type", "block")), (condition("trigger", "import"))).add(literal("import ")).add(mark("name", "firstUppercase")).add(literal(" from \"../")).add(mark("directory")).add(literal("/displays/blocks/")).add(mark("name", "firstUppercase")).add(literal("\"")),
			rule().add((condition("type", "component")), (condition("trigger", "import"))).add(literal("import ")).add(mark("name", "firstUppercase")).add(literal(" from \"../")).add(mark("directory")).add(literal("/displays/components/")).add(mark("name", "firstUppercase")).add(literal("\"")),
			rule().add((condition("type", "display")), (condition("trigger", "import"))).add(literal("import ")).add(mark("name", "firstUppercase")).add(literal(" from \"../")).add(mark("directory")).add(literal("/displays/")).add(mark("name", "firstUppercase")).add(literal("\"")),
			rule().add((condition("type", "display")), (condition("trigger", "export"))).add(mark("name", "firstUppercase"))
		);
		return this;
	}
}