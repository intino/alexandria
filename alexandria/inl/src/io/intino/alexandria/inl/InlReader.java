package io.intino.alexandria.inl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InlReader {
	private boolean hasNext = true;
	private BufferedReader reader;
	private Message current;
	private Message next;

	public InlReader(InputStream is) {
		this.reader = new BufferedReader(new InputStreamReader(is), 65536);
		try {
			this.current = createMessage(typeIn(nextLine()), null);
		} catch (Throwable e) {
			hasNext = false;
		}
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

	private static String normalize(String line) {
		return line == null ? null : isEmpty(line) ? "" : isTrimRequired(line) ? trim(line) : line;
	}

	private static boolean isTrimRequired(String line) {
		return !isMultiline(line) && !isHeaderIn(line);
	}

	private static boolean isMultiline(String line) {
		return line.charAt(0) == '\t';
	}

	private static boolean isHeaderIn(String line) {
		return !isEmpty(line) && line.charAt(0) == '[';
	}

	private static boolean isEmpty(String line) {
		char[] chars = charsOf(line);
		if (chars.length > 0 && chars[0] == '\t') return false;
		for (char c : chars)
			if (c != ' ' && c != '\t') return false;
		return true;
	}

	private static char[] charsOf(String line) {
		return line == null ? new char[0] : line.toCharArray();
	}

	private static String trim(String line) {
		int[] index = splitIndex(line.toCharArray());
		return line.substring(0, index[0] + 1) + ":" + line.substring(index[1]);
	}

	private static int[] splitIndex(char[] data) {
		int index = -1;
		while (++index < data.length) if (data[index] == ':') break;
		int[] result = new int[]{index, index};
		while (--result[0] >= 0 && data[result[0]] == ' ') ;
		while (++result[1] < data.length && data[result[1]] == ' ') ;
		return result;
	}

	public Message next() {
		if (current == null) return null;
		String attribute = "";
		Message scope = current;
		while (true) {
			String line = nextLine();
			if (line == null) return swap(null);
			else if (isMultilineIn(line)) scope.append(attribute, line.substring(1));
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
		return !isEmpty(line) && line.charAt(0) == '\t';
	}

	public boolean hasNext() {
		return hasNext;
	}

	public void close() throws IOException {
		reader.close();
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
