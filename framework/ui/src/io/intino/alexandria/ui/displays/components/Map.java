package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.MapCollectionSetup;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.behaviors.CollectionBehavior;
import io.intino.alexandria.ui.displays.components.collection.behaviors.MapCollectionBehavior;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.notifiers.MapNotifier;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.PlaceMark;

import java.net.URL;
import java.util.List;

import static io.intino.alexandria.ui.displays.components.collection.builders.PlaceMarkBuilder.buildList;

public abstract class Map<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Item, Item> extends io.intino.alexandria.ui.displays.components.AbstractMap<MapNotifier, B> implements Collection<ItemComponent, PlaceMark<Item>> {
    private Map.Type type;
    private URL kmlLayer = null;
    private URL icon = null;

    public Map(B box) {
        super(box);
    }

    public enum Type { Cluster, Kml, Heatmap }

    @Override
    public void didMount() {
        notifier.setup(setupSchema());
        notifyReady();
    }

    @Override
    public <D extends Datasource> void source(D source) {
        source(source, new MapCollectionBehavior<Item>(this));
    }

    @Override
    public ItemComponent add(PlaceMark<Item> itemPlaceMark) {
        ItemComponent component = create(itemPlaceMark);
        addPromise(component, "rows");
        promisedChildren().forEach(this::register);
        List<Display> children = children();
        for (int i=0; i<children.size(); i++) {
            final int index = i;
            addItemListener().ifPresent(l -> l.accept(itemEvent(children.get(index), index)));
        }
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
    protected AddItemEvent itemEvent(Display display, int index) {
        return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item(), index);
    }

    @Override
    void setup() {
        MapDatasource source = source();
        if (source == null && type != Type.Kml) return;
        if (source != null) {
            CollectionBehavior behavior = behavior();
            behavior.setup(source);
        }
        notifier.setup(setupSchema());
        notifyReady();
    }

    public void refreshPlaceMarks(List<PlaceMark<Item>> placeMarks) {
        notifier.placeMarks(buildList(placeMarks, baseAssetUrl()));
    }

    public void showPlaceMark(long pos) {
        MapCollectionBehavior behavior = behavior();
        behavior.showPlaceMark(pos);
    }

    private MapCollectionSetup setupSchema() {
        MapCollectionSetup setup = new MapCollectionSetup();
        if (behavior() != null) setup.itemCount(behavior().itemCount());
        if (type == Type.Kml) setup.kmlLayer(Asset.toResource(baseAssetUrl(), this.kmlLayer).toUrl().toString());
        if (icon != null) setup.icon(Asset.toResource(baseAssetUrl(), this.icon).toUrl().toString());
        return setup;
    }

}