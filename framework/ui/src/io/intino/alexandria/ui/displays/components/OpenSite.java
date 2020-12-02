package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.actionable.OpenListener;
import io.intino.alexandria.ui.displays.notifiers.OpenSiteNotifier;

public class OpenSite<DN extends OpenSiteNotifier, B extends Box> extends AbstractOpenSite<DN, B> {
	private String site;
	private OpenListener beforeOpenListener = null;
	private OpenListener openListener = null;

    public OpenSite(B box) {
        super(box);
    }

	public OpenSite<DN, B> onBeforeOpen(OpenListener listener) {
		this.beforeOpenListener = listener;
		return this;
	}

	public OpenSite<DN, B> onOpen(OpenListener listener) {
    	this.openListener = listener;
    	return this;
	}

	public void execute() {
		if (site == null) return;
		if (beforeOpenListener != null) beforeOpenListener.accept(new Event(this));
		notifier.open(site);
		if (openListener != null) openListener.accept(new Event(this));
	}

	public String site() {
		return site;
	}

	public OpenSite site(String site) {
    	_site(site);
    	return this;
	}

	public OpenSite path(String site) {
		_site(site);
		return this;
	}

	protected OpenSite _site(String site) {
		this.site = site;
		return this;
	}
}