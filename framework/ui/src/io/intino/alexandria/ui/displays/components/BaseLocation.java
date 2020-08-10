package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.LocationCenter;
import io.intino.alexandria.schemas.LocationSetup;
import io.intino.alexandria.schemas.LocationZoomRange;
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

    public Geometry value() {
        return value;
    }

    public BaseLocation<DN, B> value(String value) {
        return value(value != null ? Geometry.fromWkt(value) : null);
    }

    public BaseLocation<DN, B> value(Geometry location) {
        _value(location);
        refresh();
        return this;
    }

    public BaseLocation<DN, B> zoom(int zoom) {
        notifier.refreshZoom(zoom);
        return this;
    }

    public BaseLocation<DN, B> zoomRange(int min, int max) {
        notifier.refreshZoomRange(new LocationZoomRange().min(min).max(max));
        return this;
    }

    public BaseLocation<DN, B> center(double latitude, double longitude) {
        notifier.refreshCenter(new LocationCenter().lat(latitude).lng(longitude));
        return this;
    }

    @Override
    public void init() {
        super.init();
        setup();
        refresh();
    }

    @Override
    public void refresh() {
        notifier.refresh(value != null ? PlaceMarkBuilder.buildGeometry(value) : null);
    }

    protected <D extends BaseLocation> D _icon(URL icon) {
        this.icon = icon;
        return (D) this;
    }

    protected <D extends BaseLocation> D _value(Geometry value) {
        this.value = value;
        return (D) this;
    }

    protected <D extends BaseLocation> D _value(String value) {
        return _value(Geometry.fromWkt(value));
    }

    private void setup() {
        if (icon == null) return;
        LocationSetup setup = new LocationSetup();
        setup.icon(Asset.toResource(baseAssetUrl(), this.icon).toUrl().toString());
        notifier.setup(setup);
    }

}