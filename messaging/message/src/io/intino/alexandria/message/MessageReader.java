package io.intino.alexandria.message;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static io.intino.alexandria.message.Message.AttachmentHeader;

public class MessageReader implements Iterable<Message>, Iterator<Message> {
	private Loader loader;
	private Message next;

	public MessageReader(String str) {
        this(new ByteArrayInputStream(str.getBytes()));
    }

	public MessageReader(InputStream is) {
		this.loader = new Loader(new BufferedInputStream(is));
		this.next = loader.load();
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
		next = loader.load();
		return current;
	}

	public void close() {
        loader.close();
    }

	private static class Loader {
		private final BufferedInputStream is;
		private final List<Message> scopes;
        private Cursor cursor;

        Loader(BufferedInputStream is) {
			this.is = is;
			this.scopes = new ArrayList<>();
			this.cursor = new Cursor(is);
		}

        Message load() {
            try {
                return load(blocks());
            } catch (IOException e) {
                return null;
            }
        }

        private Message load(List<Block> blocks) throws IOException {
            return blocks.isEmpty() ? null : toMessage(blocks).put(attachments());
        }

        private Message toMessage(List<Block> blocks) {
            scopes.clear();
            for (Block block : blocks)
                scopes.add(createMessage(block));
            return scopes.get(0);
        }

        private List<Block> blocks() {
            List<Block> blocks = new ArrayList<>();
            blocks.clear();
            Block block = new Block("");
            while (cursor.hasNext()) {
                if (isHeader(cursor.next)) {
                    if (cursor.hasAttachmentHeader()) break;
                    if (cursor.hasMainHeader() && !blocks.isEmpty()) break;
                    blocks.add(block = cursor.block());
                    cursor.next();
                    continue;
                }
                block.add(cursor.next());
            }
            return blocks;
        }

        private Map<String, byte[]> attachments() throws IOException {
            if (!cursor.hasAttachmentHeader()) return new HashMap<>();
            cursor.next();
            Map<String, byte[]> attachments = new HashMap<>();
            while (cursor.hasNext()) {
                if (cursor.isEmpty())
                    cursor.next();
                else
                    attachments.put(cursor.blobId(), cursor.readBlob());
                if (cursor.hasMainHeader()) break;
            }
            return attachments;
        }

		private static Message create(Block block) {
			Message message = new Message(typeIn(block.header));
			for (String[] data : block) {
			    if (data[1].isEmpty()) continue;
                message.set(data[0], isMultiline(data[1]) ? data[1].substring(1) : data[1]);
            }
			return message;
		}

		private static String typeIn(String line) {
			String[] path = typesIn(line);
			return path[path.length - 1];
		}

		private static String[] typesIn(String line) {
			return line.split("\\.");
		}

		private Message createMessage(Block block) {
			Message owner = scopeOf(block);
			Message message = create(block);
			if (owner != null) owner.add(message);
			return message;
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

		void close() {
			try {
				is.close();
			} catch (IOException ignored) {
			}
		}
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


    private static class Cursor {
	    private InputStream is;
        private Scanner scanner;
        private String next;
        private int pos;

        Cursor(InputStream is) {
            this.is = is;
            this.mark();
        }

        boolean hasAttachmentHeader() {
            return isAttachmentHeader(next);
        }

        boolean hasMainHeader() {
            return isHeader(next) && !isInnerHeader(next);
        }

        private static boolean shouldTrim(String line) {
            return !isMultiline(line) && !isHeader(line);
        }

        private static String trim(String line) {
            int[] index = splitIndex(line.toCharArray());
            return line.substring(0, index[0] + 1) + ":" + line.substring(index[1]);
        }

        private static int[] splitIndex(char[] data) {
            int index = -1;
            while (++index < data.length) if (data[index] == ':' || data[index] == '=') break;
            int[] result = new int[]{index, index};
            do { --result[0]; } while (result[0] >= 0 && data[result[0]] == ' ');
            do { ++result[1]; } while (result[1] < data.length && data[result[1]] == ' ');
            return result;
        }

        private static String normalize(String line) {
            return line == null ? null : line.isEmpty() ? "" : shouldTrim(line) ? trim(line) : line;
        }

        boolean hasNext() {
            return next != null;
        }

        String next() {
            String line = next;
            next = readNext();
            return line;
        }

        String readNext() {
            return scanner.hasNext() ? read(scanner.nextLine()) : null;
        }

        byte[] readBlob() throws IOException {
            byte[] buffer = new byte[blobSize()];
            this.is.reset();
            this.is.skip(pos);
            this.is.read(buffer);
            this.is.skip(2);
            this.mark();
            return Base64.getDecoder().decode(buffer);
        }

        private String read(String line) {
            pos += line.length() + 1;
            return normalize(line);
        }

        String blobId() {
            return next.split(":")[0];
        }

        int blobSize() {
            return Integer.valueOf(next.split(":")[1]);
        }

        void mark() {
            this.is.mark(16384);
            this.scanner = new Scanner(is);
            this.pos = 0;
            this.next = readNext();
        }

        Block block() {
            return new Block(stripBrackets(next));
        }

        private String stripBrackets(String line) {
            return line.substring(1, line.length() - 1);
        }

        boolean isEmpty() {
            return next.isEmpty();
        }
    }

    private static boolean isHeader(String line) {
        return line != null && line.length() > 0 && line.charAt(0) == '[';
    }

    private static boolean isAttachmentHeader(String line) {
        return line != null && line.equals(AttachmentHeader);
    }

    private static boolean isInnerHeader(String line) {
        return isHeader(line) && line.contains(".");
    }

    private static boolean isMultiline(String line) {
        return line.length() > 0 && line.charAt(0) == '\t';
    }


}
