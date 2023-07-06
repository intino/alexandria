package io.intino.alexandria.sqlpredicate.parser;

public class SimpleCharStream {
	public static final boolean staticFlag = false;
	int bufSize;
	int available;
	int tokenBegin;
	public int bufPos = -1;
	protected int[] bufLine;
	protected int[] bufColumn;
	protected int column;
	protected int line;
	protected boolean prevCharIsCR = false;
	protected boolean prevCharIsLF = false;
	protected java.io.Reader inputStream;
	protected char[] buffer;
	protected int maxNextCharInd = 0;
	protected int inBuf = 0;
	protected int tabSize = 1;

	protected void expandBuff(boolean wrapAround) {
		char[] newBuffer = new char[bufSize + 2048];
		int[] newBufLine = new int[bufSize + 2048];
		int[] newBufColumn = new int[bufSize + 2048];
		try {
			if (wrapAround) {
				System.arraycopy(buffer, tokenBegin, newBuffer, 0, bufSize - tokenBegin);
				System.arraycopy(buffer, 0, newBuffer, bufSize - tokenBegin, bufPos);
				buffer = newBuffer;
				System.arraycopy(bufLine, tokenBegin, newBufLine, 0, bufSize - tokenBegin);
				System.arraycopy(bufLine, 0, newBufLine, bufSize - tokenBegin, bufPos);
				bufLine = newBufLine;
				System.arraycopy(bufColumn, tokenBegin, newBufColumn, 0, bufSize - tokenBegin);
				System.arraycopy(bufColumn, 0, newBufColumn, bufSize - tokenBegin, bufPos);
				bufColumn = newBufColumn;
				maxNextCharInd = (bufPos += (bufSize - tokenBegin));
			} else {
				System.arraycopy(buffer, tokenBegin, newBuffer, 0, bufSize - tokenBegin);
				buffer = newBuffer;
				System.arraycopy(bufLine, tokenBegin, newBufLine, 0, bufSize - tokenBegin);
				bufLine = newBufLine;
				System.arraycopy(bufColumn, tokenBegin, newBufColumn, 0, bufSize - tokenBegin);
				bufColumn = newBufColumn;
				maxNextCharInd = (bufPos -= tokenBegin);
			}
		} catch (Throwable t) {
			throw new Error(t.getMessage());
		}
		bufSize += 2048;
		available = bufSize;
		tokenBegin = 0;
	}

	protected void FillBuff() throws java.io.IOException {
		if (maxNextCharInd == available) {
			if (available == bufSize) {
				if (tokenBegin > 2048) {
					bufPos = maxNextCharInd = 0;
					available = tokenBegin;
				} else if (tokenBegin < 0) bufPos = maxNextCharInd = 0;
				else expandBuff(false);
			} else if (available > tokenBegin) available = bufSize;
			else if ((tokenBegin - available) < 2048) expandBuff(true);
			else available = tokenBegin;
		}

		int i;
		try {
			if ((i = inputStream.read(buffer, maxNextCharInd, available - maxNextCharInd)) == -1) {
				inputStream.close();
				throw new java.io.IOException();
			} else maxNextCharInd += i;
		} catch (java.io.IOException e) {
			--bufPos;
			backup(0);
			if (tokenBegin == -1)
				tokenBegin = bufPos;
			throw e;
		}
	}

	public char BeginToken() throws java.io.IOException {
		tokenBegin = -1;
		char c = readChar();
		tokenBegin = bufPos;
		return c;
	}

	protected void UpdateLineColumn(char c) {
		column++;

		if (prevCharIsLF) {
			prevCharIsLF = false;
			line += (column = 1);
		} else if (prevCharIsCR) {
			prevCharIsCR = false;
			if (c == '\n') {
				prevCharIsLF = true;
			} else
				line += (column = 1);
		}

		switch (c) {
			case '\r' -> prevCharIsCR = true;
			case '\n' -> prevCharIsLF = true;
			case '\t' -> {
				column--;
				column += (tabSize - (column % tabSize));
			}
			default -> {
			}
		}
		bufLine[bufPos] = line;
		bufColumn[bufPos] = column;
	}

	public char readChar() throws java.io.IOException {
		if (inBuf > 0) {
			--inBuf;

			if (++bufPos == bufSize)
				bufPos = 0;

			return buffer[bufPos];
		}

		if (++bufPos >= maxNextCharInd)
			FillBuff();

		char c = buffer[bufPos];

		UpdateLineColumn(c);
		return c;
	}

	public int getEndColumn() {
		return bufColumn[bufPos];
	}

	public int getEndLine() {
		return bufLine[bufPos];
	}

	public int getBeginColumn() {
		return bufColumn[tokenBegin];
	}

	public int getBeginLine() {
		return bufLine[tokenBegin];
	}

	public void backup(int amount) {
		inBuf += amount;
		if ((bufPos -= amount) < 0) bufPos += bufSize;
	}

	public SimpleCharStream(java.io.Reader stream, int startLine, int startColumn, int bufferSize) {
		inputStream = stream;
		line = startLine;
		column = startColumn - 1;
		available = bufSize = bufferSize;
		buffer = new char[bufferSize];
		bufLine = new int[bufferSize];
		bufColumn = new int[bufferSize];
	}

	public SimpleCharStream(java.io.Reader stream, int startLine, int startColumn) {
		this(stream, startLine, startColumn, 4096);
	}

	public SimpleCharStream(java.io.Reader dstream) {
		this(dstream, 1, 1, 4096);
	}

	public void reInit(java.io.Reader dstream, int startline, int startcolumn, int buffersize) {
		inputStream = dstream;
		line = startline;
		column = startcolumn - 1;

		if (buffer == null || buffersize != buffer.length) {
			available = bufSize = buffersize;
			buffer = new char[buffersize];
			bufLine = new int[buffersize];
			bufColumn = new int[buffersize];
		}
		prevCharIsLF = prevCharIsCR = false;
		tokenBegin = inBuf = maxNextCharInd = 0;
		bufPos = -1;
	}

	public void reInit(java.io.Reader stream, int startLine, int startColumn) {
		reInit(stream, startLine, startColumn, 4096);
	}

	public void reInit(java.io.Reader stream) {
		reInit(stream, 1, 1, 4096);
	}

	public SimpleCharStream(java.io.InputStream stream, String encoding, int startLine, int startColumn, int bufferSize) throws java.io.UnsupportedEncodingException {
		this(encoding == null ? new java.io.InputStreamReader(stream) : new java.io.InputStreamReader(stream, encoding), startLine, startColumn, bufferSize);
	}

	public SimpleCharStream(java.io.InputStream stream, String encoding, int startline, int startColumn) throws java.io.UnsupportedEncodingException {
		this(stream, encoding, startline, startColumn, 4096);
	}

	public void reInit(java.io.InputStream stream, String encoding, int startLine, int startColumn, int bufferSize) throws java.io.UnsupportedEncodingException {
		reInit(encoding == null ? new java.io.InputStreamReader(stream) : new java.io.InputStreamReader(stream, encoding), startLine, startColumn, bufferSize);
	}

	public void reInit(java.io.InputStream stream, String encoding, int startLine, int startColumn) throws java.io.UnsupportedEncodingException {
		reInit(stream, encoding, startLine, startColumn, 4096);
	}

	public String getImage() {
		if (bufPos >= tokenBegin)
			return new String(buffer, tokenBegin, bufPos - tokenBegin + 1);
		else
			return new String(buffer, tokenBegin, bufSize - tokenBegin) +
					new String(buffer, 0, bufPos + 1);
	}
}
