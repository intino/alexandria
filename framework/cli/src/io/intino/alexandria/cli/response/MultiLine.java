package io.intino.alexandria.cli.response;

import io.intino.alexandria.cli.Response;
import io.intino.alexandria.cli.util.MessageHelper;

import java.util.List;
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
		return lines.stream().filter(this::isVisible).map(this::serialize).filter(l -> !l.isEmpty()).collect(Collectors.joining(""));
	}

	private String serialize(Line line) {
		String result = line(line);
		if (result.isEmpty()) return "";
		return result + (line.addBreak() ? "\n" : " ");
	}

	private boolean isVisible(Line line) {
		if (line.dependantLine() == null) return true;
		Line dependant = findLine(line.dependantLine());
		if (dependant == null) return false;
		if (dependant.multiple().value()) {
			List<MessageData> data = provider.dataList(dependant.name());
			return data != null && !data.isEmpty();
		}
		return provider.data(dependant.name()) != null;
	}

	private String line(Line line) {
		String template = line.template();
		List<MessageData> data = line.multiple().value() ? provider.dataList(line.name()) : singletonList(provider.data(line.name()));
		Line.Multiple.Arrangement arrangement = line.multiple().arrangement();
		List<String> result = data.stream().map(d -> lineOf(d, template)).filter(l -> l != null && !l.isEmpty()).collect(Collectors.toList());
		if (result.isEmpty()) return "";
		return String.join(arrangement == Line.Multiple.Arrangement.Vertical ? "\n" : " ", result);
	}

	private String lineOf(MessageData data, String template) {
		return MessageHelper.replaceVariables(template, data);
	}

	private Line findLine(String name) {
		return lines.stream().filter(l -> l.name().equals(name)).findFirst().orElse(null);
	}

}
