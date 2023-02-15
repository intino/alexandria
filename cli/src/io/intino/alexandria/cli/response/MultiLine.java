package io.intino.alexandria.cli.response;

import io.intino.alexandria.cli.Response;
import io.intino.alexandria.cli.util.MessageHelper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class MultiLine extends Response {
	private List<Line> lines;
	private final MultiLineProvider provider;

	public MultiLine(List<Line> lines, MultiLineProvider provider) {
		this.lines = lines;
		this.provider = provider;
	}

	@Override
	public String toString() {
		return lines.stream().filter(this::isVisible).map(this::line).filter(l -> !l.isEmpty()).collect(Collectors.joining("\n"));
	}

	private boolean isVisible(Line line) {
		if (line.dependantLine() == null) return true;
		Line dependant = findLine(line.dependantLine());
		if (dependant == null) return false;
		MessageData data = provider.data(dependant.name());
		if (dependant.multiple()) return data != null && !data.isEmpty();
		return data != null;
	}

	private String line(Line line) {
		String template = line.template();
		List<MessageData> data = line.multiple() ? provider.dataList(line.name()) : singletonList(provider.data(line.name()));
		List<String> result = data.stream().map(d -> lineOf(d, template)).filter(Objects::nonNull).collect(Collectors.toList());
		return String.join("\n", result);
	}

	private String lineOf(MessageData data, String template) {
		return MessageHelper.replaceVariables(template, data);
	}

	private Line findLine(String name) {
		return lines.stream().filter(l -> l.name().equals(name)).findFirst().orElse(null);
	}

}
