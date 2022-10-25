package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Geometry;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.LocationEditableNotifier;
import io.intino.alexandria.ui.model.locations.Point;
import io.intino.alexandria.ui.model.locations.Polygon;
import io.intino.alexandria.ui.model.locations.Polyline;

import java.util.List;
import java.util.stream.Collectors;

public class LocationEditable<DN extends LocationEditableNotifier, B extends Box> extends AbstractLocationEditable<DN, B> implements Editable<DN, B> {
	private boolean readonly;
	private ChangeListener changeListener = null;

    public LocationEditable(B box) {
        super(box);
    }

	@Override
	public boolean readonly() {
		return readonly;
	}

	@Override
	public void reload() {
		refresh();
	}

	@Override
	public LocationEditable<DN, B> focus() {
		notifier.refreshFocused(true);
		return this;
	}

	public LocationEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

	@Override
	public LocationEditable<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void notifyChange(Geometry geometry) {
    	if (geometry == null) value((io.intino.alexandria.ui.model.Geometry) null);
    	else if (geometry.type() == Geometry.Type.Polygon) value(polygonOf(geometry));
    	else if (geometry.type() == Geometry.Type.Polyline) value(polylineOf(geometry));
    	else if (geometry.type() == Geometry.Type.Point) value(pointOf(geometry));
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value()));
	}

	protected LocationEditable<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	private Point pointOf(Geometry geometry) {
		io.intino.alexandria.schemas.Point point = geometry.point();
		return new Point(point.lat(), point.lng());
	}

	private Polyline polylineOf(Geometry geometry) {
		Polyline result = new Polyline();
		geometry.path().pointList().forEach(p -> result.add(pointOf(p)));
		return result;
	}

	private Polygon polygonOf(Geometry geometry) {
    	Polygon result = new Polygon();
		geometry.paths().forEach(p -> result.add(pointsOf(p.pointList())));
		return result;
	}

	private List<Point> pointsOf(List<io.intino.alexandria.schemas.Point> points) {
		return points.stream().map(this::pointOf).collect(Collectors.toList());
	}

	private Point pointOf(io.intino.alexandria.schemas.Point point) {
		return new Point(point.lat(), point.lng());
	}
}