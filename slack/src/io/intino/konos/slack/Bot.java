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

public abstract class Bot {
	private static Logger logger = Logger.getGlobal();

	private final String token;
	private final Map<String, Command> commands = new LinkedHashMap<>();
	private final Map<String, CommandInfo> commandsInfo = new LinkedHashMap<>();
	private SlackSession session;


	public Bot(String token) {
		this.token = token;
	}

	protected String help() {
		StringBuilder help = new StringBuilder();
		for (String key : commandsInfo.keySet())
			help.append(key).append(parameters(commandsInfo.get(key).parameters)).append(": ").append(commandsInfo.get(key).description).append("\n");
		return help.toString();
	}

	public Map<String, CommandInfo> getCommandsInfo() {
		return commandsInfo;
	}

	private String parameters(List<String> parameters) {
		return parameters.isEmpty() ? "" : " `" + String.join("` `", parameters) + "`";
	}

	public void execute() throws IOException {
		session = createWebSocketSlackSession(token);
		session.addMessagePostedListener(this::talk);
		session.connect();
	}

	private void talk(SlackMessagePosted message, SlackSession session) {
		if (message.getSender().isBot()) return;
		final String messageContent = StringEscapeUtils.unescapeHtml4(message.getMessageContent());
		String[] content = (message.getSlackFile() != null) ? contentFromSlackFile(message.getSlackFile()) : Arrays.stream(messageContent.split(" ")).filter(s -> !s.trim().isEmpty()).toArray(String[]::new);
		Command command = commands.containsKey(content[0].toLowerCase()) ? commands.get(content[0].toLowerCase()) : commandNotFound();
		try {
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
		};
	}

	protected SlackSession session() {
		return session;
	}

	protected void add(String name, List<String> parameters, List<String> components, String description, Command command) {
		commands.put(name, command);
		commandsInfo.put(name, new CommandInfo(parameters, components, description));
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
	}

	public class CommandInfo {
		private final List<String> parameters;
		private List<String> components;
		private final String description;

		CommandInfo(List<String> parameters, List<String> components, String description) {
			this.parameters = parameters;
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
