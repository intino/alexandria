package io.intino.konos.builder.codegeneration.datahub.messagehub;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MessageHubTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("messagehub","jms"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n\npublic class MessageHub extends io.intino.alexandria.event.JmsEventHub {\n\tpublic MessageHub(String url, String user, String password, String clientId, File cacheDirectory) {\n\t\tsuper(url, user, password, clientId, cacheDirectory);\n\t}\n}")),
			rule().condition((type("messagehub"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n\nimport io.intino.alexandria.message.Message;\n\nimport java.util.ArrayList;\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.Map;\nimport java.util.function.Consumer;\n\nimport static java.util.Collections.emptyList;\n\npublic class MessageHub implements io.intino.alexandria.message.MessageHub {\n\tprivate Map<String, List<Consumer<Message>>> consumers = new HashMap<>();\n\n\t@Override\n\tpublic void sendMessage(String channel, Message message) {\n\t\tnew Thread(() -> consumers.getOrDefault(channel, emptyList()).forEach(l -> l.accept(message))).start();\n\t}\n\n\t@Override\n\tpublic void attachListener(String channel, Consumer<Message> onMessageReceived) {\n\t\tif(!consumers.containsKey(channel)) consumers.put(channel, new ArrayList<>());\n\t\tconsumers.get(channel).add(onMessageReceived);\n\t}\n\n\t@Override\n\tpublic void attachListener(String channel, String subscriberId, Consumer<Message> onMessageReceived) {\n\t\tattachListener(channel, onMessageReceived);\n\t}\n\n\t@Override\n\tpublic void detachListeners(String channel) {\n\t\tconsumers.remove(channel);\n\t}\n\n\t@Override\n\tpublic void detachListeners(Consumer<Message> consumer) {\n\t\tconsumers.values().forEach(l -> l.remove(consumer));\n\t}\n\n\n\t@Override\n\tpublic void attachRequestListener(String channel, RequestConsumer onMessageReceived) {\n\t}\n\n\t@Override\n\tpublic void requestResponse(String channel, String message, Consumer<String> onResponse) {\n\t}\n}"))
		);
	}
}