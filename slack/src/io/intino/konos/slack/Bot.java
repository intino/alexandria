package io.intino.konos.slack;

import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.ullink.slack.simpleslackapi.impl.SlackSessionFactory.createWebSocketSlackSession;
import static java.util.stream.Collectors.toMap;

public abstract class Bot {
	private static Logger logger = Logger.getGlobal();
	protected Map<String, Context> usersContext = new LinkedHashMap<>();
	private final String token;
	private final Map<String, Command> commands = new LinkedHashMap<>();
	private final Map<String, CommandInfo> commandsInfo = new LinkedHashMap<>();
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

	private String parameters(List<String> parameters) {
		return parameters.isEmpty() ? "" : " `" + String.join("` `", parameters) + "`";
	}

	public void execute() throws IOException {
		session = createWebSocketSlackSession(token);
		session.addMessagePostedListener(this::talk);
		session.connect();
		initContexts();
	}

	private void initContexts() {
		for (SlackUser slackUser : session.getUsers()) usersContext.put(slackUser.getUserName(), new Context(""));
	}

	private void talk(SlackMessagePosted message, SlackSession session) {
		try {
			if (message.getSender().isBot()) return;
			final String messageContent = StringEscapeUtils.unescapeHtml4(message.getMessageContent());
			String[] content = (message.getSlackFile() != null) ? contentFromSlackFile(message.getSlackFile()) : Arrays.stream(messageContent.split(" ")).filter(s -> !s.trim().isEmpty()).toArray(String[]::new);
			String userName = message.getSender().getUserName();
			CommandInfo commandInfo = commandsInfo.get((contexts().get(userName).command.isEmpty() || isBundledCommand(content[0].toLowerCase()) ? "" : contexts().get(userName).command + "$") + content[0].toLowerCase());
			final String context = commandInfo != null ? commandInfo.context : "";
			final String commandKey = (context.isEmpty() ? "" : context + "$") + content[0].toLowerCase();
			Command command = commandInfo != null && commands.containsKey(commandKey) && isInContext(commandKey, userName) ? commands.get(commandKey) : commandNotFound();
			final Object response = command.execute(createMessageProperties(message), content.length > 1 ? Arrays.copyOfRange(content, 1, content.length) : new String[0]);
			if (response == null || (response instanceof String && response.toString().isEmpty())) return;
			if (response instanceof String) session.sendMessage(message.getChannel(), response.toString());
			else if (response instanceof SlackAttachment)
				session.sendMessage(message.getChannel(), "", (SlackAttachment) response);
		} catch (Throwable e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			session.sendMessage(message.getChannel(), "Command Error. Try `help` to see the options");
		}
	}

	private String[] contentFromSlackFile(SlackFile slackFile) {
		return new String[]{slackFile.getComment().trim(), slackFile.getUrlPrivateDownload()};
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
		return session.getChannels().stream().filter(c -> c.getId().equals(channel)).findFirst().orElse(null);
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

	}

	public class CommandInfo {
		private final List<String> parameters;
		private String context;
		private List<String> components;
		private final String description;

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
