import java.time.Instant;

public class Main {

	public static void main(String[] args) {
		String[] test = {
				"dfgjsdk   :      asdlfasdkfja",
				"dfgjsdk: asdlfasdkfja",
				"dfgjsdk: asdlfasdkfja",
				"dfgjsdk: asdlfasdkfja",
				"[asdfaksdfsa]",
				"\t",
				"\t"
		};
		System.out.println(Instant.now());
		for (int i = 0; i < 20000000; i++) {
			oldNormalize(test[i%test.length]);
		}
		System.out.println(Instant.now());
		for (int i = 0; i < 20000000; i++) {
			newNormalize(test[i%test.length]);
		}
		System.out.println(Instant.now());
	}

	private static String oldNormalize(String line) {
		if (line == null) return null;
		if (line.startsWith("\t")) return line;
		line = line.trim();
		if (line.isEmpty()) return line;
		if (line.startsWith("[")) return line;
		return line.replaceAll("(\\w*)\\s*[:=]\\s*(.*)", "$1:$2");
	}

	private static String newNormalize(String line) {
		return isEmpty(line) ? "" : isTrimRequired(line) ? trim(line) : line;
	}

	private static boolean isTrimRequired(String line) {
		return !isMultiline(line) && !isHeaderIn(line);
	}

	private static boolean isMultiline(String line) {
		return line.charAt(0) == '\t';
	}

	private static boolean isHeaderIn(String line) {
		return line.charAt(0) == '[';
	}

	private static boolean isEmpty(String line) {
		for (char c : charsOf(line))
			if (c != ' ' && c != '\t') return false;
		return true;
	}

	private static char[] charsOf(String line) {
		return line == null ? new char[0] : line.toCharArray();
	}

	private static String trim(String line) {
		int[] index = splitIndex(line.toCharArray());
		return line.substring(0, index[0]+1) + ":" + line.substring(index[1]);
	}

	private static int[] splitIndex(char[] data) {
		int index = -1;
		while (++index < data.length) if (data[index] == ':') break;
		int[] result = new int[] {index,index};
		while (--result[0] >= 0 && data[result[0]] == ' ');
		while (++result[1] < data.length && data[result[1]] == ' ');
		return result;
	}

}
