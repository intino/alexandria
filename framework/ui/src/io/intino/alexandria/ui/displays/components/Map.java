package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.MapCollectionSetup;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.behaviors.CollectionBehavior;
import io.intino.alexandria.ui.displays.components.collection.behaviors.MapCollectionBehavior;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.notifiers.MapNotifier;
import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.PlaceMark;

import java.net.URL;
import java.util.List;

import static io.intino.alexandria.ui.displays.components.geo.PlaceMarkBuilder.buildList;

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

    protected Map _type(Map.Type type) {
        this.type = type;
        return this;
    }

    protected Map _kmlLayer(URL layer) {
        this.type = Type.Kml;
        this.kmlLayer = layer;
        return this;
    }

    protected Map _icon(URL icon) {
        this.icon = icon;
        return this;
    }

    @Override
    protected AddItemEvent itemEvent(Display display) {
        return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item());
    }

    @Override
    void setup() {
        MapDatasource source = source();
        if (source == null && type != Type.Kml) return;
        MapCollectionSetup setup = new MapCollectionSetup();
        if (source != null) {
            CollectionBehavior behavior = behavior();
            behavior.setup(source);
            setup.itemCount(behavior.itemCount());
        }
        if (type == Type.Kml) setup.kmlLayer(Asset.toResource(baseAssetUrl(), this.kmlLayer).toUrl().toString());
        if (icon != null) setup.icon(Asset.toResource(baseAssetUrl(), this.icon).toUrl().toString());
        notifier.setup(setup);
        notifyReady();
    }

    public void refreshPlaceMarks(List<PlaceMark<Item>> placeMarks) {
        notifier.placeMarks(buildList(placeMarks, baseAssetUrl()));
    }

    public void showPlaceMark(long pos) {
        MapCollectionBehavior behavior = behavior();
        behavior.showPlaceMark(pos);
    }

}