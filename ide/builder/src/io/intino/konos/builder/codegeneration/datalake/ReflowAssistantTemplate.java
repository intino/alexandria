package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class ReflowAssistantTemplate extends Template {

	protected ReflowAssistantTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ReflowAssistantTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "operations & graph"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".datalake;\n\n\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.Timetag;\nimport io.intino.tara.magritte.RemounterGraph;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "FirstUpperCase")).add(literal("Box;\nimport io.intino.tara.magritte.stores.FileSystemStore;\nimport io.intino.ness.core.Datalake;\nimport io.intino.tara.magritte.Graph;\nimport org.apache.commons.io.FileUtils;\n\nimport java.io.File;\nimport java.io.IOException;\nimport java.util.List;\n\npublic class ReflowAssistant {\n\tprivate final ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\tprivate boolean cleanDirectory = true;\n\n\tReflowAssistant(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tint defaultBlockSize() {\n\t\treturn Integer.MAX_VALUE;\n\t}\n\n\tDatalake.EventStore.Reflow.Filter filter() {\n\t\treturn new Datalake.EventStore.Reflow.Filter() {\n\t\t\t@Override\n\t\t\tpublic boolean allow(Datalake.EventStore.Tank tank) {\n\t\t\t\treturn true;\n\t\t\t}\n\n\t\t\t@Override\n\t\t\tpublic boolean allow(Datalake.EventStore.Tank tank, Timetag timetag) {\n\t\t\t\treturn true;\n\t\t\t}\n\t\t};\n\t}\n\n\tvoid onStart() {\n\t\tfinal Graph graph = graph();\n\t\tif (graph.store() instanceof FileSystemStore && cleanDirectory)\n\t\t\tdeleteDirectory(((FileSystemStore) graph.store()).directory());\n\t\tfinal RemounterGraph original = (RemounterGraph) new RemounterGraph(graph.store()).loadStashes(coreStashes());\n\t\tallowWriting(original, false);\n\t\tbox.put(graph);\n\t}\n\n\tvoid onBlock() {\n\t\tallowWriting(graph(), true);\n\t\tgraph().saveAll(\"Model\");\n\t\tGraph graph = new RemounterGraph(graph().store()).loadStashes(coreStashes());\n\t\tallowWriting(graph, false);\n\t\tbox.put(graph);\n\t}\n\n\tvoid onFinish() {\n\t\tfinal Graph graph = new Graph(graph().store());\n\t\tallowWriting(graph, true);\n\t\tbox.put(graph.loadStashes(coreStashes()));\n\t}\n\n\tprivate String[] coreStashes() {\n\t\treturn new String[]{\"Model\"};\n\t}\n\n\tprivate Graph graph() {\n\t\treturn null;\n\t\t//return box.graph().core$();\n\t}\n\n\tprivate void deleteDirectory(File directory) {\n\t\ttry {\n\t\t\tFileUtils.deleteDirectory(directory);\n\t\t} catch (IOException e) {\n\t\t\tLogger.error(e);\n\t\t}\n\t}\n\n\tprivate void allowWriting(Graph original, boolean flag) {\n\t\tif (original.store() instanceof FileSystemStore) ((FileSystemStore) original.store()).allowWriting(flag);\n\t}\n}")),
				rule().add((condition("type", "operations"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".datalake;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "FirstUpperCase")).add(literal("Box;\nimport io.intino.ness.core.Datalake;\n\nimport java.util.List;\n\nclass ReflowAssistant {\n\n\tprivate final ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\n\tReflowAssistant(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tvoid onStart() {\n\n\t}\n\n\tvoid onBlock() {\n\n\t}\n\n\n\tvoid onFinish() {\n\n\t}\n\n\tint defaultBlockSize() {\n\t\treturn Integer.MAX_VALUE;\n\t}\n\n\tReflow.Filter filter() {\n\t\treturn new Reflow.Filter() {\n\t\t\t@Override\n\t\t\tpublic boolean allow(Datalake.EventStore.Tank tank) {\n\t\t\t\treturn true;\n\t\t\t}\n\n\t\t\t@Override\n\t\t\tpublic boolean allow(Datalake.EventStore.Tank tank, Timetag timetag) {\n\t\t\t\treturn true;\n\t\t\t}\n\t\t};\n\t}\n\n}"))
		);
		return this;
	}
}