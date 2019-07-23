package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.LocationSetup;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.displays.components.geo.PlaceMarkBuilder;
import io.intino.alexandria.ui.displays.notifiers.BaseLocationNotifier;
import io.intino.alexandria.ui.model.Geometry;

import java.net.URL;

public class BaseLocation<DN extends BaseLocationNotifier, B extends Box> extends AbstractBaseLocation<DN, B> {
    private URL icon = null;
    private Geometry value = null;

    public BaseLocation(B box) {
        super(box);
    }

    public <D extends BaseLocation> D icon(URL icon) {
        this.icon = icon;
        return (D) this;
    }

    public <D extends BaseLocation> D value(String value) {
        this.value = Geometry.fromWkt(value);
        return (D) this;
    }

    public <D extends BaseLocation> D update(String value) {
        return update(Geometry.fromWkt(value));
    }

    public <D extends BaseLocation> D update(Geometry location) {
        this.value = location;
        refresh();
        return (D) this;
    }

    public Geometry value() {
        return value;
    }

    @Override
    public void init() {
        super.init();
        setup();
        refresh();
    }

    public void setup() {
        if (icon == null) return;
        LocationSetup setup = new LocationSetup();
        setup.icon(Asset.toResource(baseAssetUrl(), this.icon).toUrl().toString());
        notifier.setup(setup);
    }

    public void refresh() {
        if (value == null) return;
        notifier.refresh(PlaceMarkBuilder.buildGeometry(value));
    }

}