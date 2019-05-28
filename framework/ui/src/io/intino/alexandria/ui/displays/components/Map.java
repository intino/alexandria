package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.MapCollectionSetup;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.behaviors.MapCollectionBehavior;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.notifiers.MapNotifier;
import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.datasource.PlaceMark;
import io.intino.alexandria.ui.model.datasource.locations.Location;
import io.intino.alexandria.ui.model.datasource.locations.Point;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class Map<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Item, Item> extends io.intino.alexandria.ui.displays.components.AbstractMap<MapNotifier, B> implements Collection<ItemComponent, PlaceMark<Item>> {
    private Map.Type type;
    private URL kmlLayer = null;
    private URL icon = null;

    public Map(B box) {
        super(box);
    }

    public enum Type { Cluster, Kml, Heatmap }

    public Map<B, ItemComponent, Item> source(MapDatasource source) {
        source(source, new MapCollectionBehavior<Item>(this));
        return this;
    }

    public Map type(Map.Type type) {
        this.type = type;
        return this;
    }

    public Map kmlLayer(URL layer) {
        this.type = Type.Kml;
        this.kmlLayer = layer;
        return this;
    }

    public Map icon(URL icon) {
        this.icon = icon;
        return this;
    }

    @Override
    protected AddItemEvent itemEvent(Display display) {
        return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item());
    }

    @Override
    public ItemComponent add(PlaceMark<Item> itemPlaceMark) {
        ItemComponent component = create(itemPlaceMark);
        addPromise(component, "rows");
        promisedChildren().forEach(this::register);
        children().forEach(c -> addItemListener().ifPresent(l -> l.accept(itemEvent(c))));
        notifyRefresh();
        return component;
    }

    @Override
    public List<ItemComponent> add(List<PlaceMark<Item>> placeMarks) {
        return null;
    }

    @Override
    public ItemComponent insert(PlaceMark<Item> itemPlaceMark, int index) {
        return null;
    }

    @Override
    public List<ItemComponent> insert(List<PlaceMark<Item>> placeMarks, int from) {
        return null;
    }

    @Override
    void setup() {
        MapDatasource source = source();
        if (source == null && type != Type.Kml) return;
        MapCollectionSetup setup = new MapCollectionSetup();
        if (source != null) setup.itemCount(source.itemCount());
        if (type == Type.Kml) setup.kmlLayer(Asset.toResource(baseAssetUrl(), this.kmlLayer).toUrl().toString());
        if (icon != null) setup.icon(Asset.toResource(baseAssetUrl(), this.icon).toUrl().toString());
        notifier.setup(setup);
        if (source != null) behavior().setup(source);
        notifyReady();
    }

    public void refreshPlaceMarks(List<PlaceMark<Item>> placeMarks) {
        notifier.placeMarks(placeMarksOf(placeMarks));
    }

    public void showPlaceMark(long pos) {
        MapCollectionBehavior behavior = behavior();
        behavior.showPlaceMark(pos);
    }

    private List<io.intino.alexandria.schemas.PlaceMark> placeMarksOf(List<PlaceMark<Item>> placeMarks) {
        List<io.intino.alexandria.schemas.PlaceMark> result = new ArrayList<>();
        for (int i=0; i<placeMarks.size(); i++) result.add(placeMarkOf(placeMarks.get(i), i));
        return result;
    }

    private io.intino.alexandria.schemas.PlaceMark placeMarkOf(PlaceMark<Item> placeMark, long pos) {
        io.intino.alexandria.schemas.PlaceMark result = new io.intino.alexandria.schemas.PlaceMark().location(locationOf(placeMark)).pos(pos).label(placeMark.label());
        if (placeMark.icon() != null) result.icon(Asset.toResource(baseAssetUrl(), placeMark.icon()).toUrl().toString());
        return result;
    }

    private io.intino.alexandria.schemas.Location locationOf(PlaceMark<Item> placeMark) {
        Location location = placeMark.location();
        return new io.intino.alexandria.schemas.Location().type(typeOf(location)).pointList(pointsOf(location));
    }

    private io.intino.alexandria.schemas.Location.Type typeOf(io.intino.alexandria.ui.model.datasource.locations.Location location) {
        if (location.isPolyline()) return io.intino.alexandria.schemas.Location.Type.Polyline;
        else if (location.isPolygon()) return io.intino.alexandria.schemas.Location.Type.Polygon;
        return io.intino.alexandria.schemas.Location.Type.Point;
    }

    private List<io.intino.alexandria.schemas.Location.Point> pointsOf(io.intino.alexandria.ui.model.datasource.locations.Location location) {
        return location.points().stream().map(this::pointOf).collect(toList());
    }

    protected io.intino.alexandria.schemas.Location.Point pointOf(Point p) {
        return new io.intino.alexandria.schemas.Location.Point().lat(p.latitude()).lng(p.longitude());
    }

}