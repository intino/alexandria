package io.intino.konos.builder.codegeneration.services.jms;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class JMSRequestTemplate extends Template {

	protected JMSRequestTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMSRequestTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "request"))).add(literal("package ")).add(mark("package")).add(literal(".requests;\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\nimport com.google.gson.Gson;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.jms.RequestConsumer;\n\nimport javax.jms.*;\nimport java.util.List;\nimport java.util.logging.Level;\nimport java.util.logging.Logger;\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Request implements RequestConsumer {\n\n\tprivate ")).add(mark("box", "firstUpperCase")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Request(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void consume(Session session, Message request) {\n\t\ttry {\n\t\t\t")).add(mark("call")).add(literal("\n\t\t} ")).add(mark("exception", "catch")).add(literal("\n\t\tcatch (Throwable e) {\n\t\t\torg.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).error(e.getMessage(), e);\n\t\t}\n\t}\n\n\tprivate ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action actionFor(Message message) {\n\t\tfinal ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action = new ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action();\n\t\taction.box = this.box;")).add(expression().add(literal("\n")).add(literal("\t\ttry {")).add(literal("\n")).add(literal("\t\t\t")).add(mark("parameter", "assign").multiple("\n")).add(literal("\n")).add(literal("\t\t} catch (JMSException e) {")).add(literal("\n")).add(literal("\t\t\torg.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).error(e.getMessage(), e);")).add(literal("\n")).add(literal("\t\t}"))).add(literal("\n\t\treturn action;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\tprivate Message responseMessage(Session session, String responseId, ")).add(mark("returnType")).add(literal(" response) {")).add(literal("\n")).add(literal("\t\ttry {")).add(literal("\n")).add(literal("\t\t\t")).add(mark("returnMessageType")).add(literal("Message message = session.create")).add(mark("returnMessageType")).add(literal("Message();")).add(literal("\n")).add(literal("\t\t\tmessage.setJMSCorrelationID(responseId);")).add(literal("\n")).add(literal("\t\t\t")).add(mark("returnMessageType", "return")).add(literal("\n")).add(literal("\t\t\treturn message;")).add(literal("\n")).add(literal("\t\t} catch (JMSException e) {")).add(literal("\n")).add(literal("\t\t\torg.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).error(e.getMessage(), e);")).add(literal("\n")).add(literal("\t\t\treturn null;")).add(literal("\n")).add(literal("\t\t}")).add(literal("\n")).add(literal("\t}"))).add(literal("\n}")),
			rule().add((condition("attribute", "Bytes")), (condition("trigger", "return"))).add(literal("message.writeBytes(toByteArray(response));")),
			rule().add((condition("trigger", "return"))).add(literal("message.set")).add(mark("value")).add(literal("(new Gson().toJson(response));")),
			rule().add(not(condition("type", "void")), (condition("trigger", "call"))).add(literal("response(session, replyTo(request), responseMessage(session, idOf(request), actionFor(request).execute()));")),
			rule().add((condition("trigger", "call"))).add(literal("actionFor(request).execute();")),
			rule().add((condition("trigger", "catch"))).add(literal("catch (AlexandriaException e) {\n\tresponse(session, replyTo(request), exceptionMessage(session, idOf(request), e));\n}")),
			rule().add((condition("type", "parameter & FileData")), (condition("trigger", "assign"))).add(literal("byte[] data = new byte[(int) ((BytesMessage) message).getBodyLength()];\n((BytesMessage) message).readBytes(data);\naction.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = toInputStream(data);")),
			rule().add((condition("type", "parameter & objectData")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = new Gson().fromJson(((TextMessage) message).getText(), ")).add(mark("type")).add(literal(".class);")),
			rule().add((condition("type", "parameter & List")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = new Gson().fromJson(message.getStringProperty(\"")).add(mark("name")).add(literal("\"),  new com.google.gson.reflect.TypeToken<java.util.ArrayList<")).add(mark("type")).add(literal(">>(){}.getType());")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = message.get")).add(mark("type", "format")).add(literal("Property(\"")).add(mark("name")).add(literal("\");")),
			rule().add((condition("attribute", "Integer")), (condition("trigger", "format"))).add(literal("Int")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}