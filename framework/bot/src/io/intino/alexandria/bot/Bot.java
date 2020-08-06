package io.intino.alexandria.bot;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public abstract class Bot {
	protected final String token;
	protected final Set<String> processedMessages = new LinkedHashSet<>();
	protected Map<String, Context> usersContext = new LinkedHashMap<>();
	protected final Map<String, Command> commands = new LinkedHashMap<>();
	protected final Map<String, CommandInfo> commandsInfo = new LinkedHashMap<>();


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

	public abstract void connect() throws Exception;

	public abstract void disconnect();

	public abstract void sendMessage(String channelName, String message);

	public abstract void sendAttachment(String channelName, String fileName, String title, InputStream attachment);

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

	protected void add(String name, List<String> parameters, List<String> components, String description, Command command) {
		commands.put(name, command);
		commandsInfo.put(name, new CommandInfo(parameters, "", components, description));
	}

	protected void add(String name, String context, List<String> parameters, List<String> components, String description, Command command) {
		commands.put((context.isEmpty() ? "" : context + "$") + name, command);
		commandsInfo.put((context.isEmpty() ? "" : context + "$") + name, new CommandInfo(parameters, context, components, description));
	}

	protected String decode(String message) {
		if (message.startsWith("<") && message.endsWith(">"))
			if (message.contains("|")) return message.substring(message.indexOf("|" + 1), message.length() - 1);
			else return message.substring(1, message.length() - 1);
		return message;
	}


	protected Command commandNotFound() {
		return (messageProperties, args) -> "Command not found";
	}


	private boolean isInContext(String commandKey, String userName) {
		final Context context = usersContext.get(userName);
		return isBundledCommand(commandKey) || (context != null && (context.command.isEmpty() || context.command.equalsIgnoreCase(commandKey.substring(0, commandKey.lastIndexOf("$")))));
	}

	private boolean isBundledCommand(String commandKey) {
		return commandKey.equalsIgnoreCase("help") || commandKey.equalsIgnoreCase("where") || commandKey.equalsIgnoreCase("exit");
	}

	private String parameters(List<String> parameters) {
		return parameters.isEmpty() ? "" : " `" + String.join("` `", parameters) + "`";
	}

	public interface Command {
		Object execute(MessageProperties properties, String... args);
	}

	public interface AttachedCommand extends Command {
		Attachment execute(MessageProperties properties, String... args);
	}

	public interface MessageProperties {
		String channel();

		String username();

		String ts();

		String timeZone();

		Context context();

		Attachment attachment();

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

	public static class Attachment {
		private final InputStream inputStream;
		private final String title;
		private final String fileName;

		public Attachment(InputStream inputStream, String title, String fileName) {
			this.inputStream = inputStream;
			this.title = title;
			this.fileName = fileName;
		}

		public Attachment(byte[] bytes, String title, String fileName) {
			this.inputStream = new ByteArrayInputStream(bytes);
			this.title = title;
			this.fileName = fileName;
		}

		public InputStream inputStream() {
			return inputStream;
		}

		public String tittle() {
			return title;
		}

		public String fileName() {
			return fileName;
		}
	}

	public static class CommandInfo {
		private final List<String> parameters;
		private final String description;
		private final String context;
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
