package io.intino.konos.builder.codegeneration.datahub.messagehub;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MessageHubTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("messagehub", "jms"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n\npublic class MessageHub extends io.intino.alexandria.event.JmsEventHub {\n\tpublic MessageHub(String url, String user, String password, String clientId, File cacheDirectory) {\n\t\tsuper(url, user, password, clientId, cacheDirectory);\n\t}\n}")),
			rule().condition((type("messagehub"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n\nimport io.intino.alexandria.message.Message;\n\nimport java.util.ArrayList;\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.Map;\nimport java.util.function.Consumer;\n\nimport static java.util.Collections.emptyList;\n\npublic class MessageHub implements io.intino.alexandria.message.MessageHub {\n\tprivate Map<String, List<Consumer<Message>>> consumers = new HashMap<>();\n\n    @Override\n    public void sendMessage(String channel, Message message) {\n        new Thread(() -> consumers.getOrDefault(channel, emptyList()).forEach(l -> l.accept(message))).start();\n    }\n\n    @Override\n    public void attachListener(String channel, Consumer<Message> onMessageReceived) {\n        if(!consumers.containsKey(channel)) consumers.put(channel, new ArrayList<>());\n        consumers.get(channel).add(onMessageReceived);\n    }\n\n    @Override\n    public void attachListener(String channel, String subscriberId, Consumer<Message> onMessageReceived) {\n        attachListener(channel, onMessageReceived);\n    }\n\n    @Override\n    public void detachListeners(String channel) {\n        consumers.remove(channel);\n    }\n\n    @Override\n    public void detachListeners(Consumer<Message> consumer) {\n        consumers.values().forEach(l -> l.remove(consumer));\n    }\n\n\n    @Override\n    public void attachRequestListener(String channel, RequestConsumer onMessageReceived) {\n    }\n\n    @Override\n    public void requestResponse(String channel, String message, Consumer<String> onResponse) {\n    }\n}"))
		);
	}
}