package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class NessJMXOperationsTemplate extends Template {

	protected NessJMXOperationsTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new NessJMXOperationsTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "interface"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness;\npublic interface NessOperationsMBean {\n\tboolean reflow();\n}")),
			rule().add((condition("type", "operations")), not(condition("type", "interface"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "FirstUpperCase")).add(literal("Box;\nimport io.intino.konos.jmx.JMXServer;\nimport io.intino.tara.magritte.Graph;\nimport io.intino.tara.magritte.RemounterGraph;\nimport io.intino.tara.magritte.Store;\nimport io.intino.tara.magritte.stores.FileSystemStore;\nimport org.apache.commons.io.FileUtils;\n\nimport java.io.IOException;\nimport java.time.Instant;\nimport java.util.Collections;\nimport java.util.TimerTask;\nimport java.util.Timer;\n\nimport static java.time.temporal.ChronoUnit.SECONDS;\n\npublic class NessOperations implements NessOperationsMBean {\n\n\tprivate final ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\tprivate final GraphProvider provider;\n\n\tpublic NessOperations(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t\tthis.provider = new ")).add(mark("package", "validPackage")).add(literal(".ness.GraphProvider(box);\n\t}\n\n\tpublic boolean reflow() {\n\t\tbox.close();\n\t\tfinal Graph graph = provider.graph();\n\t\tfinal Store oldStore = graph.store();\n\t\ttry {\n\t\t\tif (oldStore instanceof FileSystemStore) FileUtils.deleteDirectory(((FileSystemStore) oldStore).directory());\n\t\t\tfinal RemounterGraph original = (RemounterGraph) new RemounterGraph(provider.store()).loadStashes(graph.openedStashes());\n\t\t\tallowWriting(original, false);\n\t\t\tGraph clone = original.realClone();\n\t\t\tbox.put(clone);\n\t\t\tbox.open();\n\t\t\tnew Timer(true).schedule(timerTask(original, clone), 0);\n\t\t} catch (IOException e) {\n\t\t\te.printStackTrace();\n\t\t}\n\t\treturn true;\n\t}\n\n\tprivate TimerTask timerTask(RemounterGraph original, Graph clone) {\n\t\treturn new TimerTask() {\n\t\t\t@Override\n\t\t\tpublic void run() {\n\t\t\t\tGraph newClone = write(original, clone);\n\t\t\t\tif (finished()) {\n\t\t\t\t\tdoWrite(original, clone);\n\t\t\t\t\tonFinish(original);\n\t\t\t\t} else new Timer(true).schedule(timerTask(original, newClone), 0);\n\t\t\t}\n\t\t};\n\t}\n\n\tprivate Graph write(RemounterGraph original, Graph clone) {\n\t\tif (box.datalake().receivedMessages() < 1E6) return clone;\n\t\tbox.datalake().reset();\n\t\treturn doWrite(original, clone);\n\t}\n\n\tprivate Graph doWrite(RemounterGraph original, Graph clone) {\n\t\tbox.close();\n\t\tallowWriting(original, true);\n\t\tprovider.saveGraph(clone);\n\t\tfinal Graph newClone = original.realClone();\n\t\tallowWriting(newClone, false);\n\t\tbox.put(newClone);\n\t\tbox.open();\n\t\treturn newClone;\n\t}\n\n\tprivate void allowWriting(Graph original, boolean flag) {\n\t\tif (original.store() instanceof FileSystemStore) ((FileSystemStore) original.store()).allowWriting(flag);\n\t}\n\n\tprivate void onFinish(Graph remounterGraph) {\n\t\tbox.close();\n\t\tbox.put(new Graph(provider.store()).loadStashes(remounterGraph.openedStashes()));\n\t\tbox.open();\n\t}\n\n\tprivate boolean finished() {\n\t\treturn box.datalake().lastMessage().until(Instant.now(), SECONDS) > 5000;\n\t}\n\n\tpublic static JMXServer init(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tJMXServer server = new JMXServer(Collections.singletonMap(\"")).add(mark("package")).add(literal(".ness.NessOperations\", new Object[]{box}));\n\t\tserver.init();\n\t\treturn server;\n\t}\n}"))
		);
		return this;
	}
}