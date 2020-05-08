package io.intino.alexandria.message.parser;

import io.intino.alexandria.message2.Message;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MesssageGenerator extends InlGrammarBaseListener {

	Deque<Message> stack = new ArrayDeque<>();
	List<Message> roots = new ArrayList<>();

	@Override
	public void enterMessage(InlGrammar.MessageContext ctx) {
		Message message = new Message(ctx.type().typeName().getText());
		if (stack.isEmpty()) {
			roots.add(message);
		}
		stack.push(message);
	}

	@Override
	public void exitMessage(InlGrammar.MessageContext ctx) {
		super.exitMessage(ctx);
	}


	@Override
	public void enterAttribute(InlGrammar.AttributeContext ctx) {
		super.enterAttribute(ctx);
	}

	@Override
	public void exitAttribute(InlGrammar.AttributeContext ctx) {
		super.exitAttribute(ctx);
	}
}
