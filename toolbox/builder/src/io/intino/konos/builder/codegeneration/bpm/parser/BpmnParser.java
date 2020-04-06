package io.intino.konos.builder.codegeneration.bpm.parser;

import io.intino.alexandria.logger.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BpmnParser {

	private InputStream source;
	private Document document;
	private Map<String, String> annotations;
	private List<Node> tasks;

	public BpmnParser(String xmlText) {
		this(new ByteArrayInputStream(xmlText.getBytes()));
	}

	public BpmnParser(InputStream xmlStream) {
		source = xmlStream;
		document = this.getDOM();
		tasks = tasks();
		annotations = annotations();
	}

	public List<State> states() {
		List<State> states = tasks.stream().map(this::stateFrom).collect(Collectors.toList());
		calculateLinks(states);
		states.forEach(this::addComments);
		return states;
	}

	private void calculateLinks(List<State> states) {
		Map<String, State> map = toMap(states);
		tasks.forEach(task -> calculateLinks(map, task));
	}

	private void calculateLinks(Map<String, State> map, Node task) {
		calculateStartEvent(map.get(id(task)), task);
		List<Node> outgoings = children(task, "outgoing");
		for (Node outgoing : outgoings) {
			List<String> destinationStates = findDestinationStates(outgoing.getTextContent(), false);
			if (destinationStates.isEmpty()) calculateEndEvent(map.get(id(task)), outgoing);
			for (String destinationState : destinationStates) {
				String[] split = destinationState.split("#");
				map.get(id(task)).link(map.get(split[0]), Link.Type.valueOf(split[1]), split.length > 2);
			}
		}
	}

	private void calculateEndEvent(State state, Node outgoing) {
		streamOf(document.getElementsByTagName("bpmn:endEvent")).forEach(n -> {
			Node incoming = child(n, "incoming");
			if (incoming != null && incoming.getTextContent().equals(outgoing.getTextContent()))
				state.type(State.Type.Terminal);
		});
	}

	private void calculateStartEvent(State state, Node task) {
		Node outgoing = child(document.getElementsByTagName("bpmn:startEvent").item(0), "outgoing");
		Node incoming = child(task, "incoming");
		if (outgoing != null && incoming != null && outgoing.getTextContent().equals(incoming.getTextContent()))
			state.type(State.Type.Initial);
	}

	private List<String> findDestinationStates(String outgoingId, boolean isDefault) {
		List<String> states = directIncoming(outgoingId, isDefault);
		if (!states.isEmpty()) return states;
		return throughGateway(outgoingId);
	}

	private List<String> directIncoming(String outgoingId, boolean isDefault) {
		for (Node item : tasks) {
			List<Node> incomings = children(item, "incoming");
			for (Node incoming : incomings)
				if (incoming != null && outgoingId.equalsIgnoreCase(incoming.getTextContent()))
					return List.of(id(item) + "#" + Link.Type.Line + (isDefault ? "#Default" : ""));
		}
		return Collections.emptyList();
	}

	private List<Node> tasks() {
		NodeList items = document.getDocumentElement().getElementsByTagName("bpmn:process").item(0).getChildNodes();
		return IntStream.range(0, items.getLength()).mapToObj(items::item).
				filter(item -> item.getNodeName().endsWith("Task") || item.getNodeName().equals("bpmn:task")).
				collect(Collectors.toList());
	}

	private List<String> throughGateway(String outgoingId) {
		Node processNode = document.getElementsByTagName("bpmn:process").item(0);
		NodeList childNodes = processNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node gateway = childNodes.item(i);
			if (gateway.getNodeName().endsWith("Gateway")) {
				Node incoming = child(gateway, "incoming");
				String defaultBranch = attribute(gateway, "default");
				if (incoming != null && incoming.getTextContent().equals(outgoingId))
					return children(gateway, "outgoing").stream().
							map(n -> findDestinationStates(n.getTextContent(), n.getTextContent().equals(defaultBranch))).flatMap(List::stream).
							map(s -> s.replace("#" + Link.Type.Line, "#" + Link.Type.valueOf(firstUpperCase(cleanGatewayType(gateway))))).
							collect(Collectors.toList());
			}
		}
		return Collections.emptyList();
	}

	private String cleanGatewayType(Node child) {
		return child.getNodeName().replace("bpmn:", "").replace("Gateway", "");
	}

	private State stateFrom(Node item) {
		State state = new State(id(item), name(item));
		if (!item.getNodeName().equals("bpmn:task"))
			state.taskType(State.TaskType.valueOf(firstUpperCase(cleanTaskType(item))));

		return state;
	}

	private void addComments(State state) {
		if (annotations.containsKey(state.id())) {
			state.comment(annotations.get(state.id()));
		}
		if (state.type().equals(State.Type.Initial) && state.comment() == null) {
			state.comment(annotations.get(id(document.getElementsByTagName("bpmn:startEvent").item(0))));
		}
	}

	private Map<String, String> annotations() {
		return streamOf(document.getElementsByTagName("bpmn:textAnnotation")).
				collect(Collectors.toMap(n -> findState(id(n)), n -> child(n, "text").getTextContent(), (a, b) -> b));
	}

	private String findState(String textAnnotationId) {
		return streamOf(document.getElementsByTagName("bpmn:association")).
				filter(node -> attribute(node, "targetRef").equals(textAnnotationId)).findFirst().
				map(n -> attribute(n, "sourceRef")).get();
	}

	private Stream<Node> streamOf(NodeList nodeList) {
		return IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item);
	}

	private Node child(Node item, String type) {
		NodeList childNodes = item.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeName().equalsIgnoreCase("bpmn:" + type))
				return child;
		}
		return null;
	}

	private List<Node> children(Node item, String type) {
		NodeList childNodes = item.getChildNodes();
		return IntStream.range(0, childNodes.getLength()).mapToObj(childNodes::item).filter(child -> child.getNodeName().equalsIgnoreCase("bpmn:" + type)).collect(Collectors.toList());
	}

	private Map<String, State> toMap(List<State> states) {
		return states.stream().collect(Collectors.toMap(State::id, s -> s));
	}

	private String id(Node item) {
		return attribute(item, "id");
	}

	private String name(Node item) {
		return attribute(item, "name");
	}

	private String attribute(Node item, String name) {
		Node namedItem = item.getAttributes().getNamedItem(name);
		return namedItem == null ? null : namedItem.getNodeValue();
	}

	private Document getDOM() {
		Document document = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			document = dBuilder.parse(source);
			document.getDocumentElement().normalize();
		} catch (Exception e) {
			Logger.error(e);
		}
		return document;
	}

	public static String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	private String cleanTaskType(Node child) {
		return child.getNodeName().replace("bpmn:", "").replace("Task", "");
	}
}
