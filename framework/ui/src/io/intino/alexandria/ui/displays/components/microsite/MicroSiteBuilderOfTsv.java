package io.intino.alexandria.ui.displays.components.microsite;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zip.Zip;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MicroSiteBuilderOfTsv extends MicroSiteBuilder {
	private final Config config;
	private final Translator translator;

	public MicroSiteBuilderOfTsv(Config config) {
		this(config, null);
	}

	public MicroSiteBuilderOfTsv(Config config, Translator translator) {
		this.config = config;
		this.translator = translator;
	}

	public void generate(File tsv, File out) {
		try {
			Zip zip = new Zip(out);
			String content = new String(Files.readAllBytes(tsv.toPath()));
			List<String> lines = Arrays.asList(content.split("\n"));
			addTsv(zip, content);
			generatePages(zip, lines);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void addTsv(Zip zip, String tsv) throws IOException {
		zip.write("data.tsv", tsv);
	}

	private void generatePages(Zip zip, List<String> lines) throws IOException {
		int countPages = pageOf(lines.size())+1;
		int currentPage = 0;
		StringBuilder collection = new StringBuilder();
		for (int i=0; i<lines.size(); i++) {
			int page = pageOf(i);
			if (page != currentPage) {
				generatePage(zip, page-1, countPages, collection);
				collection = new StringBuilder();
			}
			collection.append(toRow(lines.get(i)));
		}
		generatePage(zip, countPages-1, countPages, collection);
	}

	private void generatePage(Zip zip, int page, int countPages, StringBuilder collection) throws IOException {
		String pageContent = generatePage(page+1, countPages, collection);
		if (page == 0) zip.write("index.html", pageContent);
		zip.write(page+1 + ".html", pageContent);
	}

	private String generatePage(int page, int countPages, StringBuilder collection) {
		String content = template();
		String options = IntStream.range(0, countPages).mapToObj(j -> "<option value='" + (j+1) + "'" + (page == j ? " selected" : "") + ">" + translate("Page") + " " + (j+1) + "</option>").collect(Collectors.joining());
		content = content.replace("::logo::", config.logo());
		content = content.replace("::title::", config.title());
		content = content.replace("::description::", config.description() != null ? config.description() : "");
		content = content.replace("::transactionHeader::", toHeaderRow(config.columns()));
		content = content.replace("::transactions::", collection);
		content = content.replace("::previousPage::", String.valueOf(page-1));
		content = content.replace("::nextPage::", String.valueOf(page+1));
		content = content.replace("::noTransactionsDisplay::", collection.length() > 0 ? "none" : "block");
		content = content.replace("::toolbarDisplay::", collection.length() > 0 ? "block" : "none");
		content = content.replace("::currentPage::", translate("Page") + " " + page + " " + translate("de") + " " + countPages);
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

	private String toHeaderRow(List<Config.Column> columns) {
		return "<tr>" + columns.stream().map(c -> "<th>" + c.label + "</th>").collect(Collectors.joining()) + "</tr>";
	}

	private String toRow(String line) {
		String[] values = line.split("\t");
		return "<tr>" + config.columns().stream().map(c -> "<td>" + values[c.index] + "</td>").collect(Collectors.joining()) + "</tr>";
	}

	private int pageOf(long current) {
		if (current == 0) return 0;
		return (int) (Math.floor(current / config.pageSize) + (current % config.pageSize > 0 ? 1 : 0)) - 1;
	}

	public static class Config {
		private String title;
		private String logo;
		private String description;
		private int pageSize = 10_000;
		private List<Column> columns = new ArrayList<>();

		public String title() {
			return title;
		}

		public Config title(String title) {
			this.title = title;
			return this;
		}

		public String logo() {
			return logo;
		}

		public Config logo(String logo) {
			this.logo = logo;
			return this;
		}

		public String description() {
			return description;
		}

		public Config description(String description) {
			this.description = description;
			return this;
		}

		public int pageSize() {
			return pageSize;
		}

		public Config pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public List<Column> columns() {
			return columns;
		}

		public Config add(Column column) {
			this.columns.add(column);
			return this;
		}

		public Config columns(List<Column> columns) {
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
