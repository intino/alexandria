package io.intino.konos;

public class CompilerMessage {
	public static final String ERROR = "error";
	public static final String WARNING = "warning";
	public static final String INFORMATION = "information";
	private final String category;
	private final String message;
	private final String url;
	private final int lineNum;
	private final int columnNum;

	public CompilerMessage(String category, String message, String url, int lineNum, int columnNum) {
		this.category = category;
		this.message = message;
		this.url = url;
		this.lineNum = lineNum;
		this.columnNum = columnNum;
	}

	public String getCategory() {
		return category;
	}

	public String getMessage() {
		return message;
	}

	public String getUrl() {
		return url;
	}

	public int getLineNum() {
		return lineNum;
	}

	public int getColumnNum() {
		return columnNum;
	}
}
