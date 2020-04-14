package io.intino.alexandria.slack;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.SlackConfig;
import com.github.seratch.jslack.api.methods.MethodsClient;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.request.files.FilesUploadRequest;
import com.github.seratch.jslack.api.methods.request.users.UsersConversationsRequest;
import com.github.seratch.jslack.api.methods.request.users.UsersListRequest;
import com.github.seratch.jslack.api.methods.response.users.UsersConversationsResponse;
import com.github.seratch.jslack.api.methods.response.users.UsersListResponse;
import com.github.seratch.jslack.api.model.Conversation;
import com.github.seratch.jslack.api.model.File;
import com.github.seratch.jslack.api.model.User;
import com.github.seratch.jslack.api.rtm.RTMClient;
import com.github.seratch.jslack.common.json.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.intino.alexandria.logger.Logger;
import org.jetbrains.annotations.Nullable;

import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static javax.websocket.CloseReason.CloseCodes.NORMAL_CLOSURE;

public abstract class Bot {
	private final String token;
	private final Map<String, Command> commands = new LinkedHashMap<>();
	private final Map<String, CommandInfo> commandsInfo = new LinkedHashMap<>();
	private final Set<String> processedMessages = new LinkedHashSet<>();
	protected Map<String, Context> usersContext = new LinkedHashMap<>();
	private List<User> users;
	private Slack slack;
	private RTMClient rtm;
	private Gson gson = GsonFactory.createCamelCase(SlackConfig.DEFAULT);
	private JsonParser jsonParser = new JsonParser();
	private User botIdentity;
	private MethodsClient methods;

	public Bot(String token) {
		this.token = token;
	}

	protected String help() {
		StringBuilder help = new StringBuilder("commands:\n");
		for (String key : commandsInfo.keySet())
			help.append(key).append(parameters(commandsInfo.get(key).parameters)).append(": ").append(commandsInfo.get(key).description).append("\n");
		return help.toString();
	}

	public Map<String, CommandInfo> commandsInfo() {
		return commandsInfo;
	}

	public Map<String, CommandInfo> commandsInfoByContext(String context) {
		return commandsInfo.keySet().stream().
				filter(command -> commandsInfo.get(command).context.equalsIgnoreCase(context)).
				collect(toMap(command -> command, commandsInfo::get, (a, b) -> b, LinkedHashMap::new));
	}

	public Map<String, Context> contexts() {
		return usersContext;
	}

	public void connect() throws IOException, DeploymentException {
		slack = instance();
		rtm = slack.rtm(token);
		init();
		rtm.connect();
		botIdentity = rtm.getConnectedBotUser();
		rtm.addMessageHandler(this::handleMessage);
		rtm.addErrorHandler(this::handleError);
		rtm.addCloseHandler(this::handleRtmClose);
	}

	private Slack instance() {
		String prefix = "https";
		String proxyUrl = System.getProperty("https.proxyHost");
		String proxyPort = System.getProperty("https.proxyPort");
		if (proxyUrl == null) {
			prefix = "http";
			proxyUrl = System.getProperty("http.proxyHost");
			proxyPort = System.getProperty("http.proxyPort");
		}
		if (proxyUrl != null) {
			SlackConfig slackConfig = new SlackConfig();
			slackConfig.setProxyUrl(prefix + "://" + proxyUrl + ":" + proxyPort);
			return Slack.getInstance(slackConfig);
		}
		return Slack.getInstance();
	}

	public void disconnect() {
		try {
			this.rtm.disconnect();
			this.slack.close();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public void rtmDisconnect() {
		try {
			this.rtm.disconnect();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public void reconnect() {
		try {
			this.rtm.reconnect();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public void sendToUser(String userName, String message) {
		User user = findUserByName(userName);
		if (user == null) return;
		sendToUser(user, message);
	}

	private void sendToUser(User user, String message) {
		try {
			methods.chatPostMessage(ChatPostMessageRequest.builder().asUser(true).channel(user.getId()).text(message).build());
		} catch (IOException | SlackApiException e) {
			Logger.error("Error sending message to" + user.getName() + ": " + message);
			Logger.error(e);
		}
	}

	public void sendToUser(String userName, String fileName, String title, InputStream attachment) {
		final User user = findUserByName(userName);
		if (user == null) return;
		sendAttachment(user.getId(), fileName, title, attachment);
	}

	public void sendMessage(String channelName, String message) {
		try {
			Conversation conversation = conversation(channelName);
			if (conversation == null) return;
			methods.chatPostMessage(ChatPostMessageRequest.builder().asUser(true).channel(conversation.getId()).text(message).build());
		} catch (IOException | SlackApiException e) {
			Logger.error(e);
		}
	}

	public void sendAttachment(String channelName, String fileName, String title, InputStream attachment) {
		try {
			Conversation conversation = conversation(channelName);
			if (conversation == null) return;
			methods.filesUpload(FilesUploadRequest.builder().channels(List.of(conversation.getId())).filename(fileName).title(title).fileData(attachment.readAllBytes()).build());
		} catch (IOException | SlackApiException e) {
			Logger.error(e);
		}
	}

	public void sendAttachment(String channelName, byte[] attachment, String fileName, String title) {
		try {
			Conversation conversation = conversation(channelName);
			if (conversation == null) return;
			methods.filesUpload(FilesUploadRequest.builder().channels(List.of(conversation.getId())).filename(fileName).title(title).fileData(attachment).build());
		} catch (IOException | SlackApiException e) {
			Logger.error(e);
		}
	}

	public User findUserById(String user) {
		return users.stream().filter(u -> !u.isBot() && u.getId().equals(user)).findFirst().orElse(null);
	}

	public User findUserByName(String user) {
		return users.stream().filter(u -> !u.isBot() && u.getName().equals(user)).findFirst().orElse(null);
	}

	private List<Conversation> channels(User user) throws IOException, SlackApiException {
		return methods.usersConversations(UsersConversationsRequest.builder().user(user.getId()).build()).getChannels();
	}

	public Object talk(String userName, String message, MessageProperties properties) {
		String[] content = Arrays.stream(message.split(" ")).filter(s -> !s.trim().isEmpty()).map(this::decode).toArray(String[]::new);
		CommandInfo commandInfo = commandsInfo.get((contexts().get(userName).command.isEmpty() || isBundledCommand(content[0].toLowerCase()) ? "" : contexts().get(userName).command + "$") + content[0].toLowerCase());
		final String context = commandInfo != null ? commandInfo.context : "";
		final String commandKey = (context.isEmpty() ? "" : context + "$") + content[0].toLowerCase();
		Command command = commandNotFound();
		if (commandInfo != null && commands.containsKey(commandKey) && isInContext(commandKey, userName))
			command = commands.get(commandKey);
		else if (commands.containsKey(commandKey) && Objects.equals(context, "")) command = commands.get(commandKey);
		return command.execute(properties, content.length > 1 ? Arrays.copyOfRange(content, 1, content.length) : new String[0]);
	}

	protected Slack session() {
		return slack;
	}

	protected List<User> users() {
		return users;
	}

	protected void add(String name, List<String> parameters, List<String> components, String description, Command command) {
		commands.put(name, command);
		commandsInfo.put(name, new CommandInfo(parameters, "", components, description));
	}

	protected void add(String name, String context, List<String> parameters, List<String> components, String description, Command command) {
		commands.put((context.isEmpty() ? "" : context + "$") + name, command);
		commandsInfo.put((context.isEmpty() ? "" : context + "$") + name, new CommandInfo(parameters, context, components, description));
	}

	private void init() {
		try {
			methods = slack.methods(token);
			UsersListResponse response = methods.usersList(UsersListRequest.builder().token(token).build());
			this.users = response.getMembers();
			response.getMembers().stream().filter(u -> !u.isBot() && !u.isAppUser()).forEach(member -> usersContext.put(member.getName(), new Context("")));
		} catch (IOException | SlackApiException e) {
			Logger.error(e);
		}
	}

	private void handleMessage(String m) {
		JsonObject json = jsonParser.parse(m).getAsJsonObject();
		JsonElement type = json.get("type");
		if (type != null && type.getAsString().equals("message")) {
			com.github.seratch.jslack.api.model.Message request = gson.fromJson(json, com.github.seratch.jslack.api.model.Message.class);
			try {
				User user = findUserById(request.getUser());
				if (user == null) return;
				String userName = user.getName();
				if (userName == null || isAlreadyProcessed(request) || isMine(request)) return;
				Object response = talk(userName, request.getText(), createMessageProperties(request.getChannel(), userName, request.getTs(), attachment(request)));
				if (response == null || (response instanceof String && response.toString().isEmpty())) return;
				if (response instanceof SlackAttachment)
					sendToUser(userName, ((SlackAttachment) response).fileName, ((SlackAttachment) response).title, ((SlackAttachment) response).inputStream);
				else sendToUser(user, response.toString());
			} catch (Throwable e) {
				Logger.error(e);
				sendMessage(request.getChannel(), "Command Error. Try `help` to see the options");
			}
		} else if (type != null && type.getAsString().equals("goodbye")) {
			rtmDisconnect();
			reconnect();
		}
	}

	private String parameters(List<String> parameters) {
		return parameters.isEmpty() ? "" : " `" + String.join("` `", parameters) + "`";
	}

	private String decode(String message) {
		if (message.startsWith("<") && message.endsWith(">"))
			if (message.contains("|")) return message.substring(message.indexOf("|" + 1), message.length() - 1);
			else return message.substring(1, message.length() - 1);
		return message;
	}

	private boolean isMine(com.github.seratch.jslack.api.model.Message request) {
		return request.getUser().equals(botIdentity.getId());
	}

	private SlackAttachment attachment(com.github.seratch.jslack.api.model.Message request) {
		if (request.getFiles() == null || request.getFiles().isEmpty()) return null;
		try {
			File file = request.getFiles().get(0);
			return new SlackAttachment(new URL(file.getUrlPrivateDownload()).openStream(), file.getName(), file.getTitle());
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	private boolean isAlreadyProcessed(com.github.seratch.jslack.api.model.Message message) {
		final boolean added = processedMessages.add(message.getTs());
		if (processedMessages.size() > 10) processedMessages.remove(processedMessages.iterator().next());
		return !added;
	}

	private void handleError(Throwable e) {
		Logger.error(e);
		reconnect();
	}


	private void handleRtmClose(CloseReason closeReason) {
		if (closeReason.getCloseCode().equals(NORMAL_CLOSURE)) reconnect();
	}

	@Nullable
	private Conversation conversation(String channelName) throws IOException, SlackApiException {
		UsersConversationsResponse conversations = methods.usersConversations(UsersConversationsRequest.builder().user(botIdentity.getId()).build());
		Conversation conversation = conversations.getChannels().stream().filter(c -> c.getName().equals(channelName)).findFirst().orElse(null);
		if (conversation == null) {
			Logger.error("Channel " + channelName + " not found. Available channels: " + conversations.getChannels().stream().map(Conversation::getName).collect(Collectors.joining(", ")));
			return null;
		}
		return conversation;
	}

	private Command commandNotFound() {
		return (messageProperties, args) -> "Command not found";
	}

	private MessageProperties createMessageProperties(String channel, String user, String ts, SlackAttachment attachment) {
		return new MessageProperties() {
			public String channel() {
				return channel;
			}

			public String username() {
				return user;
			}

			public String ts() {
				return ts;
			}

			public String timeZone() {
				return findUserByName(user).getTz();
			}

			public Context context() {
				return usersContext.get(user);
			}

			public SlackAttachment attachment() {
				return attachment;
			}

		};
	}

	private boolean isInContext(String commandKey, String userName) {
		final Context context = usersContext.get(userName);
		return isBundledCommand(commandKey) || (context != null && (context.command.isEmpty() || context.command.equalsIgnoreCase(commandKey.substring(0, commandKey.lastIndexOf("$")))));
	}

	private boolean isBundledCommand(String commandKey) {
		return commandKey.equalsIgnoreCase("help") || commandKey.equalsIgnoreCase("where") || commandKey.equalsIgnoreCase("exit");
	}

	public interface Command {
		Object execute(MessageProperties properties, String... args);
	}

	public interface AttachedCommand extends Command {
		SlackAttachment execute(MessageProperties properties, String... args);
	}

	public interface MessageProperties {
		String channel();

		String username();

		String ts();

		String timeZone();

		Context context();

		SlackAttachment attachment();

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

	public static class SlackAttachment {
		private final InputStream inputStream;
		private final String title;
		private final String fileName;

		public SlackAttachment(InputStream inputStream, String title, String fileName) {
			this.inputStream = inputStream;
			this.title = title;
			this.fileName = fileName;
		}

		public SlackAttachment(byte[] bytes, String title, String fileName) {
			this.inputStream = new ByteArrayInputStream(bytes);
			this.title = title;
			this.fileName = fileName;
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public String getTitle() {
			return title;
		}

		public String getFileName() {
			return fileName;
		}
	}

	public class CommandInfo {
		private final List<String> parameters;
		private final String description;
		private String context;
		private List<String> components;

		CommandInfo(List<String> parameters, String context, List<String> components, String description) {
			this.parameters = parameters;
			this.context = context;
			this.components = components;
			this.description = description;
		}

		public List<String> parameters() {
			return parameters;
		}

		public List<String> components() {
			return components;
		}

		public String description() {
			return description;
		}
	}
}
