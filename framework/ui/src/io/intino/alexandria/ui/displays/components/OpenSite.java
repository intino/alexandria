package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.OpenSiteNotifier;

public class OpenSite<DN extends OpenSiteNotifier, B extends Box> extends AbstractOpenSite<DN, B> {
	private String site;

    public OpenSite(B box) {
        super(box);
    }

	public void execute() {
		if (site == null) return;
		notifier.open(site);
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