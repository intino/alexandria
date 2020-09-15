import io.intino.alexandria.logger.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import us.dustinj.timezonemap.TimeZoneMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Instant;
import java.util.TimeZone;

public abstract class Bot extends io.intino.alexandria.bot.Bot {
	private final String name;
	private final TelegramLongPollingBot telegram;

	public Bot(String token, String name) {
		super(token);
		this.name = name;
		this.telegram = telegramDelegate();
	}

	@Override
	public void connect() throws Exception {
		try {
			ApiContextInitializer.init();
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			BotSession botSession = telegramBotsApi.registerBot(telegram);
		} catch (TelegramApiException e) {
			throw new Exception(e);
		}
	}

	@Override
	public void disconnect() {
		telegram.onClosing();
	}

	public void sendToUser(String userName, String message) {
		sendMessage(chatId(userName), message);
	}

	public void sendToUser(String userName, String fileName, String attachmentName, InputStream attachment) {
		sendAttachment(chatId(userName), fileName, attachmentName, attachment);
	}

	@Override
	public void sendMessage(String channelName, String message) {
		try {
			telegram.execute(new SendMessage(channelName, message));
		} catch (TelegramApiException e) {
			Logger.error(e);
		}
	}

	@Override
	public void sendAttachment(String channelName, String fileName, String title, InputStream attachment) {
		try {
			telegram.execute(new SendDocument().setDocument(fileName, attachment).setChatId(channelName));
		} catch (TelegramApiException e) {
			Logger.error(e);
		}
	}

	protected abstract String chatId(String userName);

	private TelegramLongPollingBot telegramDelegate() {
		return new TelegramLongPollingBot() {

			@Override
			public void onUpdateReceived(Update update) {
				Message m = update.getMessage();
				if (!acessAllowed(m.getContact())) return;
				try {
					String userName = m.getChat().getUserName();
					if (userName == null) return;
					Object response = talk(userName, m.getText(), createMessageProperties(m.getChatId(), userName, m.getDate(), timeZone(m.getLocation()), m.hasDocument() ? attachment(m.getDocument()) : null));
					if (response == null || (response instanceof String && response.toString().isEmpty())) return;
					if (response instanceof Attachment)
						sendAttachment(String.valueOf(m.getChatId()), ((Attachment) response).fileName(), ((Attachment) response).tittle(), ((Attachment) response).inputStream());
					else sendMessage(String.valueOf(m.getChatId()), response.toString());
				} catch (Throwable e) {
					Logger.error(e);
					sendMessage(String.valueOf(m.getChatId()), "Command Error. Try `help` to see the options");
				}
			}

			private Attachment attachment(Document document) {
				try {
					File file = telegram.downloadFile(telegram.execute(new GetFile().setFileId(document.getFileId())));
					return new Attachment(new FileInputStream(file), document.getFileName(), document.getFileName());
				} catch (TelegramApiException | FileNotFoundException e) {
					Logger.error(e);
					return null;
				}
			}

			@Override
			public String getBotUsername() {
				return name;
			}

			@Override
			public String getBotToken() {
				return token;
			}
		};
	}

	protected abstract acessAllowed(Contact contact)

	private TimeZone timeZone(Location location) {
		TimeZoneMap map = TimeZoneMap.forRegion(location.getLatitude(), location.getLongitude(), location.getLatitude(), location.getLongitude());
		return TimeZone.getTimeZone(map.getOverlappingTimeZone(location.getLatitude(), location.getLongitude()).getZoneId());
	}

	private MessageProperties createMessageProperties(Long chatId, String userName, Integer ts, TimeZone timeZone, Attachment attachment) {
		return new MessageProperties() {
			@Override
			public String channel() {
				return String.valueOf(chatId);
			}

			@Override
			public String username() {
				return userName;
			}

			@Override
			public String ts() {
				return Instant.ofEpochSecond(ts).toString();
			}

			@Override
			public String timeZone() {
				return timeZone.getID();
			}

			@Override
			public io.intino.alexandria.bot.Bot.Context context() {
				return usersContext.get(userName);
			}

			@Override
			public Attachment attachment() {
				return attachment;
			}
		};
	}

	public static class Context {
		private String command;
		private String[] objects;

		public Context(String command, String... objects) {
			this.command = command;
			this.objects = objects;
		}

		public String command() {
			return command;
		}

		public void command(String command) {
			this.command = command;
		}

		public String[] getObjects() {
			return objects;
		}

		public void objects(String... objects) {
			this.objects = objects;
		}
	}
}
