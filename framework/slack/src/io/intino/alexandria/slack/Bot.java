package io.intino.alexandria.slack;

import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import io.intino.alexandria.logger.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.Proxy;
import java.util.*;

import static com.ullink.slack.simpleslackapi.impl.SlackSessionFactory.getSlackSessionBuilder;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

public abstract class Bot {
	private final String token;
	private final Map<String, Command> commands = new LinkedHashMap<>();
	private final Map<String, CommandInfo> commandsInfo = new LinkedHashMap<>();
	private final Set<String> processedMessages = new LinkedHashSet<>();
	protected Map<String, Context> usersContext = new LinkedHashMap<>();
	private SlackSession session;

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

	public void execute() throws IOException {
		SlackSessionFactory.SlackSessionFactoryBuilder builder = getSlackSessionBuilder(token).withAutoreconnectOnDisconnection(true).withConnectionHeartbeat(0, null);
		if(System.getProperty("http.proxyHost") != null)
			builder.withProxy(Proxy.Type.HTTP, System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")));
		if(System.getProperty("https.proxyHost") != null)
			builder.withProxy(Proxy.Type.HTTP, System.getProperty("https.proxyHost"), Integer.parseInt(System.getProperty("https.proxyPort")));
		session = builder.build();
		session.addMessagePostedListener(this::talk);
		session.connect();
		initContexts();
	}


	public void disconnect() {
		try {
			this.session.disconnect();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private String parameters(List<String> parameters) {
		return parameters.isEmpty() ? "" : " `" + String.join("` `", parameters) + "`";
	}

	private void initContexts() {
		for (SlackUser slackUser : session.getUsers()) usersContext.put(slackUser.getUserName(), new Context(""));
	}

	private void talk(SlackMessagePosted message, SlackSession session) {
		try {
			if (message.getSender().isBot() || isAlreadyProcessed(message) || isMine(message)) return;
			final String messageContent = message.getSlackFile() != null ? messageContent(message) : unescapeHtml4(message.getMessageContent());

			String userName = message.getSender().getUserName();
			Object response = talk(userName, messageContent, createMessageProperties(message));
			if (response == null || (response instanceof String && response.toString().isEmpty())) return;
			if (response instanceof String) session.sendMessage(message.getChannel(), response.toString());
			else if (response instanceof SlackAttachment) session.sendMessage(message.getChannel(), "", (SlackAttachment) response);
		} catch (Throwable e) {
			Logger.error(e);
			session.sendMessage(message.getChannel(), "Command Error. Try `help` to see the options");
		}
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

	private String decode(String message) {
		if (message.startsWith("<") && message.endsWith(">") && message.contains("|"))
			return message.substring(message.indexOf("|" + 1), message.length() - 1);
		return message;
	}

	private String messageContent(SlackMessagePosted message) {
		String comment = message.getSlackFile().getComment();
		String str = "ha comentado: ";
		return comment != null ? unescapeHtml4(comment) : message.getMessageContent().substring(message.getMessageContent().indexOf(str) + str.length());
	}

	private boolean isAlreadyProcessed(SlackMessagePosted message) {
		final boolean added = processedMessages.add(message.getTimestamp());
		if (processedMessages.size() > 10) processedMessages.remove(processedMessages.iterator().next());
		return !added;
	}

	private boolean isMine(SlackMessagePosted message) {
		return session.sessionPersona().getId().equals(message.getSender().getId());
	}

	private Command commandNotFound() {
		return (messageProperties, args) -> "Command not found";
	}

	private MessageProperties createMessageProperties(SlackMessagePosted message) {
		return new MessageProperties() {
			public String channel() {
				return message.getChannel().getName();
			}

			public String username() {
				return message.getSender().getUserName();
			}

			public String userRealName() {
				return message.getSender().getRealName();
			}

			public String userTimeZone() {
				return message.getSender().getTimeZone();
			}

			public Context context() {
				return usersContext.get(message.getSender().getUserName());
			}

			@Override
			public BotFile file() {
				return message.getSlackFile() == null ? null : new BotFile(message.getSlackFile());
			}
		};
	}

	protected SlackSession session() {
		return session;
	}

	protected void add(String name, List<String> parameters, List<String> components, String description, Command command) {
		commands.put(name, command);
		commandsInfo.put(name, new CommandInfo(parameters, "", components, description));
	}

	protected void add(String name, String context, List<String> parameters, List<String> components, String description, Command command) {
		commands.put((context.isEmpty() ? "" : context + "$") + name, command);
		commandsInfo.put((context.isEmpty() ? "" : context + "$") + name, new CommandInfo(parameters, context, components, description));
	}

	public void send(String channelDestination, String message) {
		SlackChannel channel = slackChannel(channelDestination);
		if (channel == null) return;
		session.sendMessage(channel, message);
	}

	public void sendToUser(String user, String message) {
		SlackUser slackUser = session.findUserById(user);
		if (slackUser == null) slackUser = session.findUserByUserName(user);
		if (slackUser == null) return;
		session.sendMessageToUser(slackUser, message, null);
	}

	public void sendToUser(String userID, String message, File attachment) {
		final SlackUser user = session.findUserById(userID);
		if (user == null) return;
		session.sendMessageToUser(user, message, attachment != null ? new SlackAttachment(attachment.getName(), "", "", "") : null);
	}

	public void sendFile(String channelDestination, String name, byte[] content) {
		SlackChannel channel = slackChannel(channelDestination);
		if (channel == null) return;
		session.sendFile(channel, content, name);
	}

	private SlackChannel slackChannel(String channel) {
		return session.getChannels().stream().filter(c -> channel.equals(c.getId()) || channel.equals(c.getName())).findFirst().orElse(null);
	}

	private boolean isInContext(String commandKey, String userName) {
		final Context context = usersContext.get(userName);
		return isBundledCommand(commandKey) || (context != null && (context.command.isEmpty() || context.command.equalsIgnoreCase(commandKey.substring(0, commandKey.lastIndexOf("$")))));
	}

	private boolean isBundledCommand(String commandKey) {
		return commandKey.equalsIgnoreCase("help") || commandKey.equalsIgnoreCase("where") || commandKey.equalsIgnoreCase("exit");
	}

	public interface TextCommand extends Command {
		String execute(MessageProperties properties, String... args);
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

		String userRealName();

		String userTimeZone();

		Context context();

		BotFile file();

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

	public class BotFile {
		String name;
		String url;

		public BotFile(SlackFile file) {
			this.name = file.getName();
			this.url = unescapeHtml4(file.getUrlPrivateDownload()).replace("<|>", "");
		}

		public String name() {
			return name;
		}

		public String url() {
			return url;
		}

		public String textContent() {
			try {
				HttpClient client = HttpClientBuilder.create().build();
				HttpGet request = new HttpGet(url);
				request.addHeader("Authorization", "Bearer " + token);
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				if (entity != null) try (InputStream stream = entity.getContent()) {
					StringBuilder builder = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
					String line;
					while ((line = reader.readLine()) != null) builder.append(line).append("\n");
					return builder.toString();
				}
			} catch (Exception ignored) {
			}
			return "";
		}
	}

}
