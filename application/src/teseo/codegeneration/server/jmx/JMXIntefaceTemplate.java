package teseo.codegeneration.server.jmx;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JMXIntefaceTemplate extends Template {

	protected JMXIntefaceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMXIntefaceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "jmx"))).add(literal("package ")).add(mark("package", "validname")).add(literal(".jmx;\n\npublic interface ")).add(mark("name", "firstUpperCase")).add(literal("MBean {\n\n    ")).add(mark("action").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "action")), (condition("trigger", "action"))).add(expression().add(mark("returnType")).or(expression().add(literal("void")))).add(literal(" execute")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("parameter").multiple(", ")).add(literal(");"))
		);
		return this;
	}
}