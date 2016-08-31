package teseo.codegeneration.server.jmx;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JMXServerTemplate extends Template {

	protected JMXServerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMXServerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "jmx"))).add(literal("package ")).add(mark("package", "validname")).add(literal(";\n\nimport org.siani.teseo.jmx.JMXServer;\nimport tara.magritte.Graph;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("JMX {\n\n\tpublic JMXServer init(int port, Graph graph) {\n\t\tJMXServer server = new JMXServer(mbClasses(graph));\n\t\tserver.init(port);\n\t\treturn server;\n\t}\n\n\tprivate Map<String, Object[]> mbClasses(Graph graph) {\n        Map<String, Object[]> map = new HashMap<>();\n        map.put(\"")).add(mark("package")).add(literal(".jmx.")).add(mark("trigger", "firstUpperCase")).add(literal("\", new Object[]{graph});\n        return map;\n\t}\n}"))
		);
		return this;
	}
}