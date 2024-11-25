package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Resource;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.components.Chat;
import io.intino.alexandria.ui.model.chat.ChatDatasource;
import io.intino.alexandria.ui.model.chat.Message;
import io.intino.alexandria.ui.model.chat.MessageReader;
import io.intino.alexandria.ui.model.chat.buckets.BucketMessageReader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ChatExamplesMold extends AbstractChatExamplesMold<UiFrameworkBox> {

	public ChatExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		init(chat1, false);
		init(chat2, false);
		init(chat3, false);
		init(chat4, true);
	}

	private void init(Chat<?, ?> chat, boolean delayed) {
		chat.source(chatDatasource(delayed));
		chat.refresh();
	}

	private ChatDatasource chatDatasource(boolean delayed) {
		File chatStore = new File("/tmp/chat1");
		File attachmentsDir = new File(chatStore, "attachments");
		if (!chatStore.exists()) chatStore.mkdirs();
		if (!attachmentsDir.exists()) attachmentsDir.mkdirs();
		return new ChatDatasource() {

			@Override
			public MessageReader messages() {
				return new BucketMessageReader(chatStore);
			}

			@Override
			public void send(String message, ResponseReceiver receiver) {
				if (delayed) sendDelayed(message, receiver);
				else sendAll(message, receiver);
			}

			@Override
			public void send(String message, List<Resource> attachments, ResponseReceiver receiver) {
				attachments.forEach(this::save);
				if (delayed) sendDelayed(message, receiver);
				else sendAll(message, receiver);
			}

			private void save(Resource resource) {
				try {
					Files.write(new File(attachmentsDir, resource.name()).toPath(), resource.bytes());
				} catch (IOException e) {
					Logger.error(e);
				}
			}

			@Override
			public URL attachmentUrl(Message message, String name) {
				try {
					return new File(attachmentsDir, name).toURI().toURL();
				} catch (MalformedURLException e) {
					Logger.error(e);
					return null;
				}
			}

			private void sendAll(String message, ResponseReceiver receiver) {
				receiver.create("Message received: " + message).end();
			}

			private void sendDelayed(String message, ResponseReceiver receiver) {
				ResponseReceiver.MessageBuffer buffer = receiver.create("");
				schedule(e1 -> {
					buffer.add("Message");
					schedule(e2 -> {
						buffer.add(" received");
						schedule(e3 -> {
							buffer.add(": ");
							schedule(e4 -> {
								buffer.add("**" + message + "**");
								schedule(e5 -> buffer.end());
							});
						});
					});
				});
			}

			private void schedule(Consumer<Boolean> consumer) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						consumer.accept(true);
					}
				}, 700);
			}

		};
	}

}