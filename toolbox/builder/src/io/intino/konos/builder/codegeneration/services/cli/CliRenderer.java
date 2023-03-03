package io.intino.konos.builder.codegeneration.services.cli;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Parameter;
import io.intino.konos.model.Service;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;

public class CliRenderer extends Renderer {

	private final List<Service.CLI> services;

	public CliRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.services = graph.serviceList(Service::isCLI).map(Service::asCLI).collect(Collectors.toList());
	}

	@Override
	public void render() {
		services.forEach(this::processService);
	}

	private void processService(Service.CLI service) {
		writeService(service);
		writeAuthenticator(service);
		writeCli(service);
		writeCommands(service);
	}

	private void writeService(Service.CLI service) {
		final FrameBuilder builder = buildFrame(service).add("service");
		String className = snakeCaseToCamelCase(service.name$()) + "Service";
		writeFrame(gen(Target.Server), className, template().render(builder));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(gen(Target.Server), className).getAbsolutePath()));
	}

	private void writeAuthenticator(Service.CLI service) {
		final FrameBuilder builder = buildFrame(service).add("authenticator");
		String className = snakeCaseToCamelCase(service.name$()) + "ServiceAuthenticator";
		File srcFile = javaFile(src(Target.Server), className);
		if (srcFile.exists()) return;
		writeFrame(src(Target.Server), className, template().render(builder));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(src(Target.Server), className).getAbsolutePath()));
	}

	private void writeCli(Service.CLI service) {
		final FrameBuilder builder = buildFrame(service);
		String className = snakeCaseToCamelCase(service.name$());
		writeFrame(gen(Target.Server), className, template().render(builder));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(gen(Target.Server), className).getAbsolutePath()));
	}

	private void writeCommands(Service.CLI service) {
		for (Service.CLI.Command command : service.commandList()) processCommand(command);
		List<Service.CLI.Command> confirmationCommands = confirmationCommands(service);
		confirmationOptions(service).forEach(o -> processConfirmationCommand(o, commandsOf(confirmationCommands, o)));
	}

	private List<String> confirmationOptions(Service.CLI service) {
		return confirmationCommands(service).stream().map(c -> c.response().asConfirmation().options()).flatMap(Collection::stream).distinct().collect(Collectors.toList());
	}

	private List<Service.CLI.Command> confirmationCommands(Service.CLI service) {
		return service.commandList().stream().filter(c -> c.response().isConfirmation()).collect(Collectors.toList());
	}

	private List<Service.CLI.Command> commandsOf(List<Service.CLI.Command> commandList, String option) {
		return commandList.stream().filter(c -> c.response().asConfirmation().options().contains(option)).collect(Collectors.toList());
	}

	private void processCommand(Service.CLI.Command command) {
		FrameBuilder builder = buildFrame(command);
		String className = snakeCaseToCamelCase(command.name$()) + "Command";
		writeFrame(new File(gen(Target.Server) + File.separator + "cli" + File.separator + "commands"), className, commandTemplate().render(builder));
		className = snakeCaseToCamelCase(command.name$()) + "Action";
		File srcFile = javaFile(new File(src(Target.Server) + File.separator + "actions"), className);
		if (!srcFile.exists()) writeFrame(new File(src(Target.Server) + File.separator + "actions"), className, actionTemplate().render(builder));
	}

	private void processConfirmationCommand(String option, List<Service.CLI.Command> commandList) {
		FrameBuilder builder = buildConfirmationCommand(option, commandList);
		String className = snakeCaseToCamelCase(option) + "Command";
		writeFrame(new File(gen(Target.Server) + File.separator + "cli" + File.separator + "commands"), className, commandTemplate().render(builder));
	}

	private FrameBuilder buildConfirmationCommand(String option, List<Service.CLI.Command> commandList) {
		FrameBuilder result = buildBaseFrame().add("command").add("confirmation");
		result.add("option", option);
		commandList.forEach(c -> result.add("condition", commandConditionFrame(c, option)));
		return result;
	}

	private FrameBuilder commandConditionFrame(Service.CLI.Command command, String option) {
		FrameBuilder result = new FrameBuilder("condition");
		result.add("option", option);
		result.add("command", command.name());
		result.add("commandName", command.name$());
		return result;
	}

	private FrameBuilder buildFrame(Service.CLI service) {
		FrameBuilder result = buildBaseFrame().add("cli");
		Service.CLI.State initialState = service.stateList().stream().filter(Service.CLI.State::isInitial).findFirst().orElse(null);
		result.add("name", service.name$());
		result.add("initialState", initialState != null ? initialState.name$() : "");
		service.commandList().forEach(c -> result.add("command", commandFrameOf(c)));
		confirmationOptions(service).forEach(o -> result.add("confirmation", buildConfirmationCommand(o, emptyList())));
		return result;
	}

	private FrameBuilder buildFrame(Service.CLI.Command command) {
		FrameBuilder result = buildBaseFrame().add("command");
		result.add("name", command.name$());
		for (int i=0; i<command.parameterList().size(); i++)
			result.add("parameter", frameOf(command.parameterList().get(i), i));
		result.add("execute", executeFrameOf(command));
		result.add("response", frameOf(command.response()));
		return result;
	}

	private Object executeFrameOf(Service.CLI.Command command) {
		FrameBuilder result = buildBaseFrame().add("execute");
		result.add("name", command.name$());
		if (command.response().isMultiLine()) result.add("multiline");
		if (command.response().isConfirmation()) {
			Service.CLI.Command.Response.Confirmation confirmation = command.response().asConfirmation();
			result.add("confirmation");
			result.add("question", confirmation.question());
			confirmation.options().forEach(o -> result.add("option", o));
		}
		result.add("response", frameOf(command.response()));
		return result;
	}

	private Object frameOf(Parameter parameter, int index) {
		FrameBuilder result = new FrameBuilder("parameter");
		if (parameter.isList()) result.add("list");
		result.add("name", parameter.name$());
		result.add("index", index);
		return result;
	}

	private FrameBuilder frameOf(Service.CLI.Command.Response response) {
		FrameBuilder result = buildBaseFrame().add("response");
		result.add("command", response.core$().ownerAs(Service.CLI.Command.class).name$());
		if (response.isText()) result.add("text");
		if (response.isConfirmation()) {
			result.add("confirmation");
			response.asConfirmation().options().forEach(o -> result.add("option", frameOf(response, o)));
		}
		if (response.isMultiLine()) {
			result.add("multiline");
			response.asMultiLine().lineList().forEach(l -> result.add("line", frameOf(response, l)));
		}
		return result;
	}

	private FrameBuilder frameOf(Service.CLI.Command.Response response, Service.CLI.Command.Response.MultiLine.Line line) {
		FrameBuilder result = new FrameBuilder("line");
		if (line.isMultiple()) result.add("multiple");
		result.add("name", line.name$());
		result.add("addBreak", line.addBreak());
		result.add("multiple", multipleFrame(line));
		result.add("content", line.content());
		if (line.visibleWith() != null) result.add("dependant", line.visibleWith().name$());
		return result;
	}

	private FrameBuilder multipleFrame(Service.CLI.Command.Response.MultiLine.Line line) {
		FrameBuilder result = new FrameBuilder("multiple");
		result.add("value", line.isMultiple());
		result.add("arrangement", line.isMultiple() ? line.asMultiple().arrangement().name() : Service.CLI.Command.Response.MultiLine.Line.Multiple.Arrangement.Vertical.name());
		return result;
	}

	private FrameBuilder frameOf(Service.CLI.Command.Response response, String option) {
		FrameBuilder result = new FrameBuilder("option");
		Service.CLI.Command command = response.core$().ownerAs(Service.CLI.Command.class);
		result.add("command", command.name());
		result.add("commandName", command.name$());
		result.add("value", option);
		return result;
	}

	private FrameBuilder commandFrameOf(Service.CLI.Command command) {
		FrameBuilder result = buildBaseFrame().add("command");
		result.add("name", command.name$());
		result.add("command", command.name());
		result.add("abbreviation", command.abbreviation());
		result.add("description", command.description());
		result.add("parameters", listFrame(command.parameterList().stream().map(Layer::name$).collect(Collectors.toList())));
		result.add("preconditions", listFrame(command.precondition() != null ? command.precondition().states().stream().map(Layer::name$).collect(Collectors.toList()) : emptyList()));
		if (command.postcondition() != null) result.add("postcondition", command.postcondition().state().name$());
		return result;
	}

	private FrameBuilder optionFrame(Service.CLI.Command command, String option) {
		FrameBuilder result = new FrameBuilder("option");
		result.add("value", option);
		result.add("command", command.name());
		result.add("commandName", command.name$());
		return result;
	}

	private FrameBuilder listFrame(List<String> parameterList) {
		FrameBuilder result = new FrameBuilder("list");
		if (parameterList.isEmpty()) result.add("empty");
		parameterList.forEach(p -> result.add("item", p));
		return result;
	}

	private Template template() {
		return Formatters.customize(new CliTemplate());
	}

	private Template commandTemplate() {
		return Formatters.customize(new CliCommandTemplate());
	}

	private Template actionTemplate() {
		return Formatters.customize(new CliActionTemplate());
	}

}
