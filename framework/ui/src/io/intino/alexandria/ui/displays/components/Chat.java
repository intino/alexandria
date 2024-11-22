package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.MimeTypes;
import io.intino.alexandria.Resource;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Attachment;
import io.intino.alexandria.schemas.ChatInfo;
import io.intino.alexandria.schemas.ChatMessage;
import io.intino.alexandria.ui.displays.notifiers.ChatNotifier;
import io.intino.alexandria.ui.model.chat.ChatDatasource;
import io.intino.alexandria.ui.model.chat.Message;
import io.intino.alexandria.ui.model.chat.MessageReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class Chat<DN extends ChatNotifier, B extends Box> extends AbstractChat<B> {
	private ChatDatasource source;
	private MessageReader reader;
	private String condition;
	private Resource attachment;
	private URL incomingImage;
	private URL outgoingImage;
	private URL loadingImage;
	private String emptyMessage;

	public Chat(B box) {
		super(box);
	}

	public <CP extends ChatDatasource> Chat<DN, B> source(CP value) {
		this.source = value;
		return this;
	}

	public Chat<DN, B> open() {
		notifier.open();
		return this;
	}

	public Chat<DN, B> emptyMessage(String message) {
		_emptyMessage(message);
		return this;
	}

	public void uploadAttachment(Resource value) {
		this.attachment = value;
	}

	public void add(Message message) {
		notifier.addMessages(List.of(schemaOf(message)));
	}

	public void sendMessage(String content) {
		add(Message.with(content, Message.Direction.Outgoing));
		add(Message.with("", Message.Direction.Incoming));
		source.send(content, responseReceiver());
	}

	public void sendAttachment(String content) {
		add(Message.with(content, Message.Direction.Outgoing, List.of(attachment.name())));
		add(Message.with("", Message.Direction.Incoming));
		source.send(content, List.of(attachment), responseReceiver());
	}

	public void previousMessages() {
		List<ChatMessage> messages = messages();
		if (messages.size() < PageSize) notifier.messagesStartReached();
		notifier.addPreviousMessages(messages);
	}

	@Override
	public void didMount() {
		super.didMount();
		refresh();
	}

	@Override
	public void refresh() {
		super.refresh();
		if (source == null) return;
		reader = source.messages();
		List<ChatMessage> messages = messages();
		if (messages.size() < PageSize) notifier.messagesStartReached();
		notifier.refresh(info(messages));
		if (emptyMessage != null && messages.isEmpty()) add(Message.with(translate(emptyMessage), Message.Direction.Incoming));
	}

	private ChatInfo info(List<ChatMessage> messages) {
		ChatInfo result = new ChatInfo();
		result.label(label());
		result.messages(messages);
		if (incomingImage != null) result.incomingImage(assetUrl(incomingImage));
		if (outgoingImage != null) result.outgoingImage(assetUrl(outgoingImage));
		if (loadingImage != null) result.loadingImage(assetUrl(loadingImage));
		return result;
	}

	protected void _incomingImage(URL image) {
		this.incomingImage = image;
	}

	protected void _outgoingImage(URL image) {
		this.outgoingImage = image;
	}

	protected void _loadingImage(URL image) {
		this.loadingImage = image;
	}

	protected void _emptyMessage(String message) {
		this.emptyMessage = message;
	}

	private List<ChatMessage> messages() {
		return messages(condition).stream().map(this::schemaOf).collect(toList());
	}

	private static final int PageSize = 50;
	public List<Message> messages(String condition) {
		List<Message> result = new ArrayList<>();
		for (Message next : reader) {
			if (accepts(next, condition)) result.add(next);
			if (result.size() == PageSize) break;
		}
		Collections.reverse(result);
		return result;
	}

	private boolean accepts(Message next, String condition) {
		if (condition == null) return true;
		return next.content().toLowerCase().contains(condition.toLowerCase());
	}

	private ChatMessage schemaOf(Message message) {
		return new ChatMessage().date(message.ts())
								.content(message.content())
								.direction(ChatMessage.Direction.valueOf(message.direction().name()))
								.attachments(attachments(message));
	}

	private List<Attachment> attachments(Message message) {
		return message.attachments().stream().map(a -> attachment(message, a)).filter(Objects::nonNull).collect(toList());
	}

	private Attachment attachment(Message message, String name) {
		URL url = source.attachmentUrl(message, name);
		if (url == null) return null;
		String extension = name.contains(".") ? name.substring(name.lastIndexOf(".")) : name;
		return new Attachment().filename(name).mimeType(MimeTypes.contentTypeOf(extension)).url(assetUrl(url));
	}

	private ChatDatasource.ResponseReceiver responseReceiver() {
		return new ChatDatasource.ResponseReceiver() {
			@Override
			public ChatDatasource.ResponseReceiver create(String content) {
				notifier.hideLoading();
				Chat.this.add(Message.with(content, Message.Direction.Incoming));
				return this;
			}

			@Override
			public ChatDatasource.ResponseReceiver add(String content) {
				notifier.addMessagePart(content);
				return this;
			}

			@Override
			public ChatDatasource.ResponseReceiver end() {
				notifier.hideLoading();
				return this;
			}
		};
	}

}