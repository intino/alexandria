package io.intino.pandora.slack;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

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
		String help = "";
		for (String key : commandsInfo.keySet())
			help += key + parameters(commandsInfo.get(key).parameters) + ": " + commandsInfo.get(key).description + "\n";
		return help;
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
		String[] content = Arrays.stream(message.getMessageContent().split(" ")).filter(s -> !s.trim().isEmpty()).toArray(String[]::new);
		Command command = commands.containsKey(content[0]) ? commands.get(content[0]) : commandNotFound();
		try {
			final String response = command.execute(createMessageProperties(message), content.length > 1 ? Arrays.copyOfRange(content, 1, content.length) : new String[0]);
			if (response == null || response.isEmpty()) return;
			session.sendMessage(message.getChannel(), response);
		} catch (Throwable e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			session.sendMessage(message.getChannel(), "Command Error. Try `help` to see the options");
		}
	}

	private Command commandNotFound() {
		return (messageProperties, args) -> "Command not found";
	}

	private MessageProperties createMessageProperties(SlackMessagePosted message) {
		return () -> message.getChannel().getName();
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

	public void sendFile(String channelDestination, String name, byte[] content) {
		SlackChannel channel = slackChannel(channelDestination);
		if (channel == null) return;
		session.sendFile(channel, content, name);
	}

	private SlackChannel slackChannel(String channel) {
		return session.getChannels().stream().filter(c -> c.getId().equals(channel)).findFirst().orElse(null);
	}

	public interface Command {
		String execute(MessageProperties properties, String... args);
	}


	public interface MessageProperties {
		String channel();
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
