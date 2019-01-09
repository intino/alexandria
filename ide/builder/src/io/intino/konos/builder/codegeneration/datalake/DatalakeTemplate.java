package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class DatalakeTemplate extends Template {

	protected DatalakeTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DatalakeTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "tanks"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".datalake;\n\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.nessaccessor.NessAccessor;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport io.intino.ness.core.Datalake.EventStore.MessageHandler;\n\n")).add(expression().add(mark("tankImport"))).add(literal("\n\nimport java.util.ArrayList;\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.Map;\nimport java.util.function.Predicate;\n\npublic class Datalake {\n\tprivate static Map<String, MessageHandler> handlers = new HashMap<>();\n\t")).add(mark("tank", "field").multiple("\n")).add(literal("\n\n\tpublic static void registerTanks(")).add(mark("box", "firstUppercase")).add(literal("Box box) {\n\t\tfinal String clientID = ")).add(mark("clientId")).add(literal(";\n\t\tio.intino.ness.core.Datalake.EventStore eventStore = box.nessAccessor().eventStore();\n\t\t")).add(mark("tank", "assign").multiple("\n")).add(literal("\n\t\t")).add(mark("tank", "subscribe").multiple("\n")).add(literal("\n\t}\n\n\tpublic static List<TankAccessor> tanks() {\n\t\tList<TankAccessor> tanks = new ArrayList<>();\n\t\t")).add(mark("tank", "add").multiple("\n")).add(literal("\n\t\treturn tanks;\n\t}\n\n\tpublic static Map<String, io.intino.ness.core.Datalake.EventStore.MessageHandler> handlers() {\n\t\treturn handlers;\n\t}\n\n\tpublic static List<TankAccessor> byName(List<String> names) {\n\t\treturn tanks().stream().filter(t -> names.contains(t.name())).collect(java.util.stream.Collectors.toList());\n\t}\n\n\tpublic static TankAccessor byName(String type) {\n\t\treturn tanks().stream().filter(t -> type.equals(t.name())).findFirst().orElse(null);\n\t}\n\n\tpublic static void unsubscribeAll(NessAccessor accessor) {\n\t\t")).add(mark("tank", "unsubscribe").multiple("\n")).add(literal("\n\t}\n\n\t")).add(mark("tank", "getter").multiple("\n\n")).add(literal("\n\n\t")).add(mark("tank", "class").multiple("\n\n")).add(literal("\n\n\tpublic static class TankAccessor {\n\t\tprotected final ")).add(mark("box", "firstUppercase")).add(literal("Box box;\n\t\tprivate final NessAccessor accessor;\n\t\tprivate final io.intino.ness.core.Datalake.EventStore.Tank tank;\n\n\t\tTankAccessor(")).add(mark("box", "firstUppercase")).add(literal("Box box, io.intino.ness.core.Datalake.EventStore.Tank tank) {\n\t\t\tthis.box = box;\n\t\t\tthis.accessor = box.nessAccessor();\n\t\t\tthis.tank = tank;\n\t\t}\n\n\t\tpublic String name() {\n\t\t\treturn tank.name();\n\t\t}\n\n\t\tpublic io.intino.alexandria.zim.ZimStream content() {\n\t\t\treturn tank.content();\n\t\t}\n\n\t\tpublic io.intino.alexandria.zim.ZimStream content(Predicate<io.intino.alexandria.Timetag> predicate) {\n\t\t\treturn tank.content(predicate);\n\t\t}\n\n\t\tpublic void feed(io.intino.alexandria.inl.Message... messages) {\n\t\t\tif (accessor.eventStore() instanceof io.intino.alexandria.nessaccessor.tcp.TCPEventStore)\n\t\t\t\t((io.intino.alexandria.nessaccessor.tcp.TCPEventStore) accessor.eventStore()).feed(tank.name(), messages);\n\t\t}\n\n\t\tpublic void send(io.intino.alexandria.Timetag timetag, io.intino.alexandria.inl.Message... messages) {\n\t\t\tio.intino.ness.core.memory.MemoryStage stage = new io.intino.ness.core.memory.MemoryStage();\n\t\t\tio.intino.ness.core.sessions.EventSession session = stage.createEventSession();\n\t\t\tfor (io.intino.alexandria.inl.Message message : messages) session.put(tank.name(), timetag, message);\n\t\t\tsession.close();\n\t\t\taccessor.push(stage);\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "custom")), (condition("trigger", "clientId"))).add(literal("box.configuration().get(\"")).add(mark("value")).add(literal("\");")),
			rule().add((condition("trigger", "clientId"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "tank")), (condition("trigger", "field"))).add(literal("private static TankAccessor ")).add(mark("name", "firstLowerCase")).add(literal("Accessor;")),
			rule().add((condition("type", "tank")), (condition("trigger", "assign"))).add(mark("name", "firstLowerCase")).add(literal("Accessor = new ")).add(mark("name", "firstUpperCase")).add(literal("Accessor(box, eventStore.tank(\"")).add(mark("fullname")).add(literal("\"));")),
			rule().add((condition("type", "tank")), (condition("type", "mounter | input")), (condition("trigger", "subscribe"))).add(literal("handlers.put(\"")).add(mark("messageType")).add(literal("\", (MessageHandler) ")).add(mark("name", "firstLowerCase")).add(literal("Accessor);\neventStore.subscribe(")).add(mark("name", "firstLowerCase")).add(literal("Accessor.tank).using(clientID != null ? clientID + \"-")).add(mark("messageType")).add(literal("\" : null, handlers.get(\"")).add(mark("messageType")).add(literal("\"));")),
			rule().add((condition("type", "tank")), (condition("trigger", "unsubscribe"))).add(literal("accessor.eventStore().unsubscribe(")).add(mark("name", "firstLowerCase")).add(literal("Accessor.tank);")),
			rule().add((condition("type", "tank")), (condition("trigger", "add"))).add(literal("tanks.add(Datalake.")).add(mark("name", "firstLowerCase")).add(literal("Accessor);")),
			rule().add((condition("type", "tank")), (condition("trigger", "addHandler"))).add(literal("tanks.put(\"")).add(mark("name")).add(literal("\", new ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Handler(box));")),
			rule().add((condition("type", "tank")), (condition("trigger", "getter"))).add(literal("public static TankAccessor ")).add(mark("name", "firstLowerCase")).add(literal("() {\n\treturn Datalake.")).add(mark("name", "firstLowerCase")).add(literal("Accessor;\n}")),
			rule().add((condition("type", "custom")), (condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", ")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "formatMessage"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom", "customType").multiple(""))),
			rule().add((condition("type", "tank & mounter")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Accessor extends TankAccessor implements MessageHandler {\n\n\tpublic ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Accessor(")).add(mark("box", "firstUppercase")).add(literal("Box box, io.intino.ness.core.Datalake.EventStore.Tank tank) {\n\t\tsuper(box, tank);\n\t}\n\n\tpublic void handle(io.intino.alexandria.inl.Message m) {\n\t\ttry {\n\t\t\t")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Mounter mounter = new ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Mounter();\n\t\t\tmounter.box = box;\n\t\t\tmounter.")).add(mark("type", "typeName")).add(literal(" = ")).add(mark("type", "load")).add(literal(";\n\t\t\tmounter.execute();\n\t\t} catch(Throwable e) {\n\t\t\tLogger.error(e);\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "tank & input")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("messageType", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Accessor extends TankAccessor implements MessageHandler {\n\n\tpublic ")).add(mark("messageType", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Accessor(")).add(mark("box", "firstUppercase")).add(literal("Box box, io.intino.ness.core.Datalake.EventStore.Tank tank) {\n\t\tsuper(box, tank);\n\t}\n\n\tpublic void handle(io.intino.alexandria.inl.Message m) {\n\t\ttry {\n\t\t\t")).add(mark("handler").multiple("\n")).add(literal("\n\t\t} catch(Throwable e) {\n\t\t\tLogger.error(e);\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "tank")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("messageType", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Accessor  extends TankAccessor {\n\n\tpublic ")).add(mark("messageType", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Accessor(")).add(mark("box", "firstUppercase")).add(literal("Box box, io.intino.ness.core.Datalake.EventStore.Tank tank) {\n\t\tsuper(box, tank);\n\t}\n}")),
			rule().add((condition("trigger", "handler"))).add(mark("processPackage", "lowerCase")).add(literal(".")).add(mark("processName", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Process ")).add(mark("processName", "snakeCaseToCamelCase", "FirstLowerCase")).add(literal(" = new ")).add(mark("processPackage", "lowerCase")).add(literal(".")).add(mark("processName", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Process();\n")).add(mark("processName", "snakeCaseToCamelCase", "FirstLowerCase")).add(literal(".box = box;\n")).add(mark("processName", "snakeCaseToCamelCase", "FirstLowerCase")).add(literal(".")).add(mark("type", "typeName")).add(literal(" = ")).add(mark("type", "load")).add(literal(";\n")).add(mark("processName", "snakeCaseToCamelCase", "FirstLowerCase")).add(literal(".outputs = java.util.Arrays.asList(")).add(mark("output", "quoted", "lowercase").multiple(", ")).add(literal(");\n")).add(mark("processName", "snakeCaseToCamelCase", "FirstLowerCase")).add(literal(".execute();")),
			rule().add((condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration().")).add(mark("conf", "firstLowerCase")).add(literal("Configuration.")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "tankImport"))).add(literal("import ")).add(mark("value", "validPackage")).add(literal(".datalake.mounters.*;")),
			rule().add((condition("type", "schema")), (condition("trigger", "load"))).add(literal("io.intino.alexandria.inl.Inl.fromMessage(m, ")).add(mark("package")).add(literal(".schemas.")).add(mark("name", "FirstUpperCase")).add(literal(".class)")),
			rule().add((condition("trigger", "load"))).add(literal("m")),
			rule().add((condition("type", "schema")), (condition("trigger", "typeName"))).add(mark("name", "firstLowerCase")),
			rule().add((condition("trigger", "typeName"))).add(literal("message")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}