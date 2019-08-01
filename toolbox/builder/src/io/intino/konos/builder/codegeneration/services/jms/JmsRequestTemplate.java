package io.intino.konos.builder.codegeneration.services.jms;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class JmsRequestTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("request"))).output(literal("package ")).output(mark("package")).output(literal(".requests;\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(mark("package")).output(literal(".*;\nimport com.google.gson.Gson;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.jms.RequestConsumer;\n\nimport javax.jms.*;\nimport java.util.List;\nimport java.util.logging.Level;\nimport java.util.logging.Logger;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Request implements RequestConsumer {\n\n\tprivate ")).output(mark("box", "firstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Request(")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void consume(Session session, Message request) {\n\t\ttry {\n\t\t\t")).output(mark("call")).output(literal("\n\t\t} ")).output(mark("exception", "catch")).output(literal("\n\t\tcatch (Throwable e) {\n\t\t\tio.intino.alexandria.logger.Logger.error(e.getMessage(), e);\n\t\t}\n\t}\n\n\tprivate ")).output(mark("package")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action actionFor(Message message) {\n\t\tfinal ")).output(mark("package")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action action = new ")).output(mark("package")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action();\n\t\taction.box = this.box;\n\t\t")).output(expression().output(literal("try {")).output(literal("\n")).output(literal("\t")).output(mark("parameter", "assign").multiple("\n")).output(literal("\n")).output(literal("} catch (JMSException e) {")).output(literal("\n")).output(literal("\tio.intino.alexandria.logger.Logger.error(e.getMessage(), e);")).output(literal("\n")).output(literal("}"))).output(literal("\n\t\treturn action;\n\t}\n\n\t")).output(expression().output(literal("private Message responseMessage(Session session, String responseId, ")).output(mark("returnType")).output(literal(" response) {")).output(literal("\n")).output(literal("\ttry {")).output(literal("\n")).output(literal("\t\t")).output(mark("returnMessageType")).output(literal("Message message = session.create")).output(mark("returnMessageType")).output(literal("Message();")).output(literal("\n")).output(literal("\t\tmessage.setJMSCorrelationID(responseId);")).output(literal("\n")).output(literal("\t\t")).output(mark("returnMessageType", "return")).output(literal("\n")).output(literal("\t\treturn message;")).output(literal("\n")).output(literal("\t} catch (JMSException e) {")).output(literal("\n")).output(literal("\t\tio.intino.alexandria.logger.Logger.error(e.getMessage(), e);")).output(literal("\n")).output(literal("\t\treturn null;")).output(literal("\n")).output(literal("\t}")).output(literal("\n")).output(literal("}"))).output(literal("\n}")),
				rule().condition((attribute("bytes")), (trigger("return"))).output(literal("message.writeBytes(toByteArray(response));")),
				rule().condition((trigger("return"))).output(literal("message.set")).output(mark("value")).output(literal("(new Gson().toJson(response));")),
				rule().condition(not(type("void")), (trigger("call"))).output(literal("response(session, replyTo(request), responseMessage(session, idOf(request), actionFor(request).execute()));")),
				rule().condition((trigger("call"))).output(literal("actionFor(request).execute();")),
				rule().condition((trigger("catch"))).output(literal("catch (AlexandriaException e) {\n\tresponse(session, replyTo(request), exceptionMessage(session, idOf(request), e));\n}")),
				rule().condition((allTypes("parameter", "filedata")), (trigger("assign"))).output(literal("byte[] data = new byte[(int) ((BytesMessage) message).getBodyLength()];\n((BytesMessage) message).readBytes(data);\naction.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = toInputStream(data);")),
				rule().condition((allTypes("objectdata", "parameter")), (trigger("assign"))).output(literal("action.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = new Gson().fromJson(((TextMessage) message).getText(), ")).output(mark("type")).output(literal(".class);")),
				rule().condition((allTypes("parameter", "list")), (trigger("assign"))).output(literal("action.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = new Gson().fromJson(message.getStringProperty(\"")).output(mark("name")).output(literal("\"),  new com.google.gson.reflect.TypeToken<java.util.ArrayList<")).output(mark("type")).output(literal(">>(){}.getType());")),
				rule().condition((type("parameter")), (trigger("assign"))).output(literal("action.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = message.get")).output(mark("type", "format")).output(literal("Property(\"")).output(mark("name")).output(literal("\");")),
				rule().condition((attribute("integer")), (trigger("format"))).output(literal("Int")),
				rule().condition((type("schemaimport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}