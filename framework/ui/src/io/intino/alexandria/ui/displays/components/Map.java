package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.PageCollectionSetup;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.behaviors.MapCollectionBehavior;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.notifiers.MapNotifier;
import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.datasource.PlaceMark;

import java.util.ArrayList;
import java.util.List;

public abstract class Map<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Item, Item> extends io.intino.alexandria.ui.displays.components.AbstractMap<MapNotifier, B> implements Collection<ItemComponent, PlaceMark<Item>> {

    public Map(B box) {
        super(box);
    }

    public Map<B, ItemComponent, Item> source(MapDatasource source) {
        source(source, new MapCollectionBehavior<Item>(this));
        return this;
    }

    @Override
    protected AddItemEvent itemEvent(Display display) {
        return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item());
    }

    @Override
    public ItemComponent add(PlaceMark<Item> itemPlaceMark) {
        return null;
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
        if (source == null) return;
        notifier.setup(new PageCollectionSetup().itemCount(source.itemCount()));
        behavior().setup(source);
        notifyReady();
    }

    public void refreshPlaceMarks(List<PlaceMark<Item>> placeMarks) {
        notifier.placeMarks(placeMarksOf(placeMarks));
    }

    private List<io.intino.alexandria.schemas.PlaceMark> placeMarksOf(List<PlaceMark<Item>> placeMarks) {
        List<io.intino.alexandria.schemas.PlaceMark> result = new ArrayList<>();
        for (int i=0; i<placeMarks.size(); i++) result.add(placeMarkOf(placeMarks.get(i), i));
        return result;
    }

    private io.intino.alexandria.schemas.PlaceMark placeMarkOf(PlaceMark<Item> placeMark, long pos) {
        return new io.intino.alexandria.schemas.PlaceMark().location(placeMark.location().toWkt()).pos(pos);
    }
}