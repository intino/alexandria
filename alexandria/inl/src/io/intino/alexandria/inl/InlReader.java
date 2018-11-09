package io.intino.alexandria.inl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InlReader {
	private BufferedReader reader;
	private Message current;
	private Message next;
	private boolean hasNext = true;

	public InlReader(InputStream is) {
		this.reader = new BufferedReader(new InputStreamReader(is), 65536);
		try {
			this.current = createMessage(typeIn(nextLine()), null);
		} catch (Throwable e) {
			hasNext = false;
		}
	}

	public boolean hasNext() {
		return hasNext;
	}

	public void close() throws IOException {
		reader.close();
	}

	public Message next() {
		return read();
	}

	private static boolean isAttributeIn(String line) {
		return line.contains(":");
	}

	private static String typeIn(String line) {
		String[] path = pathOf(line);
		return path[path.length - 1];
	}

	private static String[] pathOf(String line) {
		line = line.substring(1, line.length() - 1);
		return line.contains(".") ? line.split("\\.") : new String[]{line};
	}

	private Message read() {
		if (current == null) return null;
		String attribute = "";
		Message scope = current;
		while (true) {
			String line = nextLine();
			if (line == null) return swap(null);
			else if (isMultilineIn(line)) scope.write(attribute, line.substring(1));
			else if (isAttributeIn(line)) {
				String value = valueOf(line);
				scope.set(attribute = attributeOf(line), value);
				if (value != null && isAttachment(value))
					scope.attach(value.substring(1), value.contains(".") ? value.split("\\.")[1] : "", new byte[0]);
			} else if (isHeaderIn(line)) {
				Message owner = ownerIn(line);
				Message message = createMessage(typeIn(line), owner);
				if (owner == null) return swap(message);
				else scope = message;
			}
		}
	}

	private boolean isAttachment(String value) {
		return value.startsWith("@");
	}

	private boolean isHeaderIn(String line) {
		return line.startsWith("[");
	}

	private String attributeOf(String line) {
		return line.substring(0, line.indexOf(":"));
	}

	private String valueOf(String line) {
		return line.indexOf(":") + 1 < line.length() ? unwrap(line.substring(line.indexOf(":") + 1)) : null;
	}

	private String unwrap(String value) {
		return value.startsWith("\"") && value.endsWith("\"") ? value.substring(1, value.length() - 1) : value;
	}

	private boolean isMultilineIn(String line) {
		return line.startsWith("\t");
	}

	private Message swap(Message message) {
		Message result = this.current;
		this.current = message;
		return result;
	}

	private String nextLine() {
		try {
			return normalize(reader.readLine());
		} catch (IOException ignored) {
			return null;
		}
	}

	private String normalize(String line) {
		if (line == null) return null;
		if (line.startsWith("\t")) return line;
		line = line.trim();
		if (line.isEmpty()) return line;
		if (line.startsWith("[")) return line;
		return line.replaceAll("(\\w*)\\s*[:=]\\s*(.*)", "$1:$2");//TODO improve performance
	}

	private Message createMessage(String type, Message owner) {
		Message message = new Message(type, owner);
		if (owner != null) owner.add(message);
		return message;
	}

	private Message ownerIn(String line) {
		if (!line.contains(".")) return null;
		Message result = current;
		for (int i = 1; i < pathOf(line).length - 1; i++) {
			assert result != null;
			result = lastComponentOf(current);
		}
		return result;
	}

	private Message lastComponentOf(Message message) {
		return message.components().isEmpty() ? null : message.components().get(message.components().size() - 1);
	}

}
