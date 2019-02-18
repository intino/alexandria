package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Resource;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.resources.Asset;

import static io.intino.alexandria.ui.utils.UrlUtil.toURL;

public class FileValue<B extends Box> extends AbstractFileValue<B> {
	private Resource value;
	protected ChangeListener changeListener = null;

	public FileValue(B box) {
        super(box);
    }

	public Resource value() {
		return value;
	}

	public void update(Resource value) {
		this.value = value;
		notifier.refresh(Asset.toResource(toURL(session().browser().baseAssetUrl()), value.id()).toUrl().toString());
	}

	public void notifyChange(Resource value) {
		this.value = value;
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
	}

}