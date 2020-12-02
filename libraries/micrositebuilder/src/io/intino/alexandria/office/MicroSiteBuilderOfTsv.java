package io.intino.alexandria.office;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zip.Zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MicroSiteBuilderOfTsv extends MicroSiteBuilder {
	private final Configuration config;
	private final Translator translator;
	private final Map<String, String> pages = new HashMap<>();

	public MicroSiteBuilderOfTsv(Configuration config) {
		this(config, null);
	}

	public MicroSiteBuilderOfTsv(Configuration config, Translator translator) {
		this.config = config;
		this.translator = translator;
	}

	public void build(File tsv, File out) {
		try {
			Zip zip = new Zip(out);
			pages.clear();
			long count = Files.lines(tsv.toPath()).count();
			Logger.info("Generating site for " + tsv.getAbsolutePath() + ". Count pages: " + (pageOf(count)+1));
			Iterator<String> content = Files.lines(tsv.toPath()).iterator();
			if (count == 0) return;
			generateColumns(content);
			generatePages(content, count);
			zip.write(pages);
			addTsv(zip, tsv, filenameOf(out));
			Logger.info("Site " + tsv.getAbsolutePath() + " generated");
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void generateColumns(Iterator<String> lines) {
		if (config.columns().size() > 0) return;
		String header = lines.next();
		String[] columns = header.split("\t");
		List<Configuration.Column> result = new ArrayList<>();
		for (int i=0; i<columns.length; i++) result.add(new Configuration.Column(columns[i], i));
		config.columns(result);
	}

	private String filenameOf(File file) {
		return file.getName().substring(0, file.getName().lastIndexOf("."));
	}

	private void addTsv(Zip zip, File tsv, String filename) throws IOException {
		zip.write(filename + ".tsv", new FileInputStream(tsv));
	}

	private void generatePages(Iterator<String> lines, long count) {
		int countPages = pageOf(count)+1;
		int currentPage = 0;
		int i = 0;
		StringBuilder collection = new StringBuilder();
		while (lines.hasNext()) {
			int page = pageOf(i);
			if (page != currentPage) {
				generatePage(page-1, countPages, collection);
				collection = new StringBuilder();
				currentPage++;
			}
			collection.append(toRow(lines.next()));
			i++;
		}
		generatePage(countPages-1, countPages, collection);
	}

	private void generatePage(int page, int countPages, StringBuilder collection) {
		String pageContent = pageContent(page+1, countPages, collection);
		if (page == 0) pages.put("index.html", pageContent);
		pages.put(page+1 + ".html", pageContent);
	}

	private String pageContent(int page, int countPages, StringBuilder collection) {
		String content = template();
		String options = IntStream.range(0, countPages).mapToObj(j -> "<option value='" + (j+1) + "'" + (page == j+1 ? " selected" : "") + ">" + translate("Page") + " " + (j+1) + "</option>").collect(Collectors.joining());
		content = content.replace("::logo::", config.logo());
		content = content.replace("::title::", config.title());
		content = content.replace("::description::", config.description() != null ? config.description() : "");
		content = content.replace("::transactionHeader::", toHeaderRow(config.columns()));
		content = content.replace("::transactions::", collection);
		content = content.replace("::previousPage::", String.valueOf(page-1));
		content = content.replace("::nextPage::", String.valueOf(page+1));
		content = content.replace("::noTransactionsDisplay::", collection.length() > 0 ? "none" : "block");
		content = content.replace("::toolbarDisplay::", collection.length() > 0 ? "block" : "none");
		content = content.replace("::currentPage::", translate("Page") + " " + page + " " + translate("of") + " " + countPages);
		content = content.replace("::previousPageDisabled::", page == 1 ? "disabled" : "");
		content = content.replace("::nextPageDisabled::", page == countPages ? "disabled" : "");
		content = content.replace("::toolbarDisplay::", countPages > 1 ? "block" : "none");
		content = content.replace("::options::", options);
		content = content.replace("::before::", translate("Before"));
		content = content.replace("::next::", translate("Next"));
		return content;
	}

	private String translate(String word) {
		return translator != null ? translator.translate(word) : word;
	}

	private String toHeaderRow(List<Configuration.Column> columns) {
		return "<tr>" + columns.stream().map(c -> "<th>" + c.label + "</th>").collect(Collectors.joining()) + "</tr>";
	}

	private String toRow(String line) {
		String[] values = line.split("\t");
		return "<tr>" + config.columns().stream().map(c -> "<td>" + (values.length > c.index ? values[c.index] : "") + "</td>").collect(Collectors.joining()) + "</tr>";
	}

	private int pageOf(long current) {
		if (current == 0) return 0;
		if (config.pageSize == 0) return 0;
		return (int) (Math.floor((double)current / config.pageSize) + (current % config.pageSize > 0 ? 1 : 0)) - 1;
	}

	public static class Configuration {
		private String title;
		private String logo;
		private String description;
		private int pageSize = 1_000;
		private List<Column> columns = new ArrayList<>();

		public String title() {
			return title;
		}

		public Configuration title(String title) {
			this.title = title;
			return this;
		}

		public String logo() {
			return logo;
		}

		public Configuration logo(String logo) {
			this.logo = logo;
			return this;
		}

		public String description() {
			return description;
		}

		public Configuration description(String description) {
			this.description = description;
			return this;
		}

		public int pageSize() {
			return pageSize;
		}

		public Configuration pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public List<Column> columns() {
			return columns;
		}

		public Configuration add(Column column) {
			this.columns.add(column);
			return this;
		}

		public Configuration columns(List<Column> columns) {
			this.columns = columns;
			return this;
		}

		public static class Column {
			private String label;
			private int index;

			public Column(String label, int index) {
				this.label = label;
				this.index = index;
			}

			public String label() {
				return label;
			}

			public Column label(String label) {
				this.label = label;
				return this;
			}

			public int index() {
				return index;
			}

			public Column index(int index) {
				this.index = index;
				return this;
			}
		}
	}

	public interface Translator {
		String translate(String word);
	}
}
