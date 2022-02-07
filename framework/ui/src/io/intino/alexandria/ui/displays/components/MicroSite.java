package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.notifiers.MicroSiteNotifier;
import io.intino.alexandria.ui.spark.UIFile;
import io.intino.alexandria.zip.Zip;

import java.io.File;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.NoSuchFileException;

public class MicroSite<DN extends MicroSiteNotifier, B extends Box> extends AbstractMicroSite<B> {
	private java.io.File site;
	private Zip siteReader;
	private int page = 0;

    public MicroSite(B box) {
        super(box);
    }

    public MicroSite<DN, B> site(File site) {
		_site(site);
		return this;
	}

	public MicroSite<DN, B> page(String page) {
		page(normalize(page));
		return this;
	}

	public MicroSite<DN, B> page(int page) {
		this.page = page;
		return this;
	}

	@Override
	public void init() {
		super.init();
		if (site != null) refresh();
	}

	@Override
	public void refresh() {
		super.refresh();
		renderPage();
	}

	public UIFile download() {
		return new UIFile() {
			@Override
			public String label() {
				return site.getName();
			}

			@Override
			public InputStream content() {
				try {
					return new FileInputStream(site);
				} catch (FileNotFoundException e) {
					Logger.error(e);
					return new ByteArrayInputStream(new byte[0]);
				}
			}
		};
	}

	public UIFile downloadContent() {
		return new UIFile() {
			@Override
			public String label() {
				return contentEntryFilename();
			}

			@Override
			public InputStream content() {
				String content = contentEntry();
				return new ByteArrayInputStream(content != null ? content.getBytes() : new byte[0]);
			}
		};
	}

	private String contentEntryFilename() {
		return site.getName().substring(0, site.getName().lastIndexOf(".")) + ".tsv";
	}

	private String contentEntry() {
		try {
			return siteReader.read(contentEntryFilename());
		} catch (IOException e) {
			return null;
		}
	}

	protected MicroSite<DN, B> _site(URL site) {
    	try {
			_site(new File(site.toURI()));
		} catch (URISyntaxException e) {
			Logger.error(e);
		}
		return this;
	}

	protected MicroSite<DN, B> _site(File site) {
		this.site = new File(site.toURI());
		this.siteReader = this.site.exists() ? new Zip(this.site) : null;
		return this;
	}

	private void renderPage() {
		try {
			String content = siteReader != null ? siteReader.read(pageName()) : null;
			if (content == null) {
				notifier.renderPageNotFound();
				return;
			}
			notifier.renderPage(content);
		} catch (NoSuchFileException e) {
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private String pageName() {
		return page == 0 ? "index.html" : page + ".html";
	}

	private int normalize(String page) {
		try {
			return Integer.parseInt(page == null || page.equals("index.html") ? "0" : page.replace(".html", ""));
		}
		catch (NumberFormatException ex) {
			return -1;
		}
	}
}