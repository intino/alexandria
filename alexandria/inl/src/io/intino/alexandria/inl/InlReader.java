package io.intino.alexandria.inl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class InlReader implements Iterable<Message>, Iterator<Message> {
	private Parser parser;
	private Message next;

	public InlReader(InputStream is) {
		this.parser = new Parser(new BufferedReader(new InputStreamReader(is)));
		this.next = parser.read();
	}

	@Override
	public Iterator<Message> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public Message next() {
		Message current = next;
		next = parser.read();
		return current;
	}

	public void close() {
		try {
			parser.close();
		} catch (IOException ignored) {
		}
	}

	private static class Parser {

		private final BufferedReader reader;
		private final List<Message> scopes;
		private String line;

		Parser(BufferedReader reader) {
			this.reader = reader;
			this.scopes = new ArrayList<>();
			this.line = nextLine();
		}

		private static Message create(Block block) {
			Message message = new Message(typeIn(block.header));
			for (String[] data : block) {
				message.set(data[0], isMultiline(data[1]) ? data[1].substring(1) : data[1]);
				if (isAttachment(data[1])) message.attach(attachmentName(data[1]), attachmentType(data[1]), new byte[0]);
			}
			return message;
		}

		private static String attachmentName(String value) {
			return value.substring(1);
		}

		private static String attachmentType(String value) {
			return value.contains(".") ? value.split("\\.")[1] : "";
		}

		private static String typeIn(String line) {
			String[] path = typesIn(line);
			return path[path.length - 1];
		}

		private static String[] typesIn(String line) {
			return line.split("\\.");
		}

		private static String normalize(String line) {
			return line == null ? null : isEmpty(line) ? "" : isTrimRequired(line) ? trim(line) : line;
		}

		private static boolean isTrimRequired(String line) {
			return !isMultiline(line) && !isHeaderIn(line);
		}

		private static boolean isMultiline(String line) {
			return line.length() > 0 && line.charAt(0) == '\t';
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

		private static boolean isAttachment(String value) {
			return value.startsWith("@") && value.length() > 37 && isUUID(value.substring(1, 36));
		}

		private static boolean isUUID(String str) {
			try {
				if (!IntStream.of(8, 13, 18, 23).allMatch(n -> str.charAt(n) == '-')) return false;
				UUID.fromString(str);
				return true;
			} catch (Exception ignored) {
				return false;
			}
		}

		Message read() {
			return parse(blocks());
		}

		private Message parse(List<Block> blocks) {
			scopes.clear();
			for (Block block : blocks) {
				scopes.add(createMessage(block));
			}
			return scopes.isEmpty() ? null : scopes.get(0);
		}

		private Message createMessage(Block block) {
			Message owner = scopeOf(block);
			Message component = create(block);
			if (owner != null) owner.add(component);
			return component;
		}

		private Message scopeOf(Block block) {
			int depth = depthOf(block.header) - 1;
			scopes.subList(depth + 1, scopes.size()).clear();
			return depth >= 0 ? scopes.get(depth) : null;
		}

		private int depthOf(String header) {
			int count = 0;
			for (char c : header.toCharArray())
				if (c == '.') count++;
			return count;
		}

		private List<Block> blocks() {
			List<Block> blocks = new ArrayList<>();
			Block block = new Block("");
			while (line != null) {
				if (isHeaderIn(line))
					blocks.add(block = new Block(stripBrackets(line)));
				else
					block.add(line);
				line = nextLine();
				if (line == null || isMainHeaderIn(line)) break;
			}
			return blocks;
		}

		private boolean isMainHeaderIn(String line) {
			return isHeaderIn(line) && !isInnerBlockIn(line);
		}

		private boolean isInnerBlockIn(String line) {
			return line.contains(".");
		}

		private String nextLine() {
			try {
				return normalize(reader.readLine());
			} catch (IOException ignored) {
				return null;
			}
		}

		private String stripBrackets(String line) {
			return line.substring(1, line.length() - 1);
		}

		public void close() throws IOException {
			reader.close();
		}

		private static class Block implements Iterable<String[]> {
			private final String header;
			private final Stack<String> lines;

			Block(String header) {
				this.header = header;
				this.lines = new Stack<>();
			}

			void add(String line) {
				if (line.isEmpty()) return;
				lines.push(ifMultilineMerge(line));
			}

			private String ifMultilineMerge(String line) {
				return line.startsWith("\t") ? previous() + line.substring(1) : line;
			}

			private String previous() {
				String pop = lines.pop();
				return pop + (pop.endsWith(":") ? "" : "\n");
			}

			boolean isChildOf(String type) {
				return header.startsWith(type + ".");
			}

			@Override
			public Iterator<String[]> iterator() {
				return new Iterator<String[]>() {
					Iterator<String> iterator = lines.iterator();

					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public String[] next() {
						String line = iterator.next();
						int idx = line.indexOf(':');
						return new String[]{line.substring(0, idx), idx == line.length() - 1 ? "" : line.substring(idx + 1)};
					}
				};
			}
		}


	}
}
