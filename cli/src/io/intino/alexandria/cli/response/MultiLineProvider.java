package io.intino.alexandria.cli.response;

import java.util.Collections;
import java.util.List;

public class MultiLineProvider {
	protected MessageData data(String line) { return null; }
	protected List<MessageData> dataList(String line) { return Collections.emptyList(); }
}
