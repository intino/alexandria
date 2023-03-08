package io.intino.alexandria.cli;

import io.intino.alexandria.cli.command.MessageProperties;
import io.intino.alexandria.cli.response.Text;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Cli {
	public static final String COMMAND_NOT_FOUND = "Command not found";
	private final Map<String, Command> commands = new LinkedHashMap<>();
	private final Map<String, CommandInfo> commandsInfo = new LinkedHashMap<>();
	private final Map<String, String> abbreviations = new HashMap<>();
	protected Map<String, Context> contextList = new LinkedHashMap<>();

	public Cli() {
	}

	public Map<String, CommandInfo> commandsInfoByState(String state) {
		return commandsInfo.keySet().stream().
				filter(command -> commandsInfo.get(command).existsInState(state)).
				collect(toMap(command -> command, commandsInfo::get, (a, b) -> b, LinkedHashMap::new));
	}

	public Map<String, Context> contexts() {
		return contextList;
	}

	public Response talk(String token, String message, MessageProperties properties) {
		String[] content = Arrays.stream(message.split(" ")).filter(s -> !s.trim().isEmpty()).map(this::decode).toArray(String[]::new);
		if (content.length == 0) return new Text(COMMAND_NOT_FOUND);
		String cmd = translate(content[0].toLowerCase());
		String state = contexts().get(token).state().isEmpty() || isBundledCommand(cmd) ? "" : contexts().get(token).state();
		CommandInfo commandInfo = findCommandInfo(state, cmd);
		Command command = commandInfo != null && isInContext(commandInfo, token) ? commands.get(commandInfo.name()) : commandNotFound();
		String commandName = commandInfo != null ? commandInfo.name() : cmd;
		Response response = command.execute(properties, commandName, content.length > 1 ? Arrays.copyOfRange(content, 1, content.length) : new String[0]);
		updateState(token, commandInfo);
		return response;
	}

	protected String initialState() {
		return "";
	}

	protected void addQuestionOption(String option, Command command) {
		commands.put(option, command);
	}

	protected void add(String name, String abbr, String description, List<String> parameters, List<String> preConditions, String postCondition, Command command) {
		register(name, command, new CommandInfo(name, abbr, description, preConditions, postCondition, parameters));
		preConditions.forEach(p -> register(key(p, name), command, new CommandInfo(key(p, name), abbr, description, preConditions, postCondition, parameters)));
	}

	private void updateState(String token, CommandInfo info) {
		String newState = info != null ? info.postCondition : "";
		Context context = contexts().get(token);
		context.lastCommand(info);
		context.state(newState);
	}

	private CommandInfo findCommandInfo(String tokenState, String cmd) {
		CommandInfo commandInfo = commandsInfo.get(key(tokenState, cmd));
		return commandInfo != null ? commandInfo : commandsInfo.get(cmd);
	}

	private String translate(String cmd) {
		return abbreviations.getOrDefault(cmd, cmd);
	}

	private void register(String name, Command command, CommandInfo info) {
		commands.put(name, command);
		commandsInfo.put(name, info);
		abbreviations.put(info.abbr(), name);
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

	private Command commandNotFound() {
		return (properties, command, args) -> new Text(COMMAND_NOT_FOUND);
	}

	private boolean isInContext(CommandInfo info, String token) {
		final Context context = contextList.get(token);
		return isBundledCommand(info.name()) || (context != null && (context.state().isEmpty() || info.existsInState(context.state())));
	}

	private boolean isBundledCommand(String commandKey) {
		return commandKey.equalsIgnoreCase("help") ||
				commandKey.equalsIgnoreCase("where") ||
				commandKey.equalsIgnoreCase("exit");
	}

	public Context loadContext(String token) {
		if (!contextList.containsKey(token)) this.contextList.put(token, new Context(initialState()));
		return contextList.get(token);
	}

	public static class CommandInfo {
		private final String name;
		private final String abbr;
		private final String description;
		private final List<String> preConditions;
		private final String postCondition;
		private final List<String> parameters;

		CommandInfo(String name, String abbr, String description, List<String> preConditions, String postCondition, List<String> parameters) {
			this.name = name;
			this.abbr = abbr;
			this.description = description;
			this.preConditions = preConditions;
			this.postCondition = postCondition;
			this.parameters = parameters;
		}

		public String name() {
			return name;
		}

		public String abbr() {
			return abbr;
		}

		public String description() {
			return description;
		}

		public List<String> parameters() {
			return parameters;
		}

		public boolean existsInState(String state) {
			return preConditions.isEmpty() || preConditions.contains(state);
		}
	}

	private String key(String state, String command) {
		return state + command;
	}

}