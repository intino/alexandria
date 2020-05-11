package io.intino.alexandria.message.parser;

import io.intino.alexandria.message.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageGenerator extends InlGrammarBaseListener {
	private final List<Message> levelScope = new ArrayList<>();
	private Message current;
	private Message root;

	@Override
	public void enterMessage(InlGrammar.MessageContext ctx) {
		String qn = ctx.type().typeName().getText();
		String[] split = qn.split("\\.");
		int level = split.length - 1;
		current = new Message(split[level]);
		if (level == 0) root = current;
		else levelScope.get(level - 1).add(current);
		add(level);
	}

	private void add(int level) {
		if (levelScope.size() <= level) levelScope.add(level, current);
		else levelScope.set(level, current);
	}

	@Override
	public void enterAttribute(InlGrammar.AttributeContext ctx) {
		current.set(ctx.IDENTIFIER().getText(), value(ctx));
	}

	private String value(InlGrammar.AttributeContext ctx) {
		return ctx.value() != null ? ctx.value().getText() : ctx.multilineValue().getText().replace("\n\t", "\n").trim();
	}

	public Message next() {
		return root;
	}
}
