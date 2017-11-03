package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.exceptions.*;
import io.intino.konos.alexandria.framework.box.*;
import io.intino.konos.alexandria.framework.box.AlexandriaFrameworkBox;
import io.intino.konos.alexandria.framework.box.schemas.*;
import io.intino.konos.alexandria.framework.box.displays.notifiers.AlexandriaDesktopDisplayNotifier;
import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.services.push.User;


public class AlexandriaDesktopDisplay extends Display<AlexandriaDesktopDisplayNotifier> {
    private AlexandriaFrameworkBox box;

    public AlexandriaDesktopDisplay(AlexandriaFrameworkBox box) {
        super();
        this.box = box;
    }

    @Override
	protected void init() {
		super.init();

		addAndPersonify(new AlexandriaTabLayoutDisplay((box)));
		addAndPersonify(new AlexandriaMenuLayoutDisplay((box)));
	}


}