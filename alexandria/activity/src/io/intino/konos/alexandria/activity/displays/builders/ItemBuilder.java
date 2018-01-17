package io.intino.konos.alexandria.activity.displays.builders;

import com.google.gson.Gson;
import io.intino.konos.alexandria.activity.model.TimeScale;
import io.intino.konos.alexandria.activity.model.mold.Block;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.*;
import io.intino.konos.alexandria.activity.model.mold.stamps.icons.ResourceIcon;
import io.intino.konos.alexandria.activity.schemas.Item;
import io.intino.konos.alexandria.activity.schemas.ItemBlock;
import io.intino.konos.alexandria.activity.schemas.ItemStamp;
import io.intino.konos.alexandria.activity.schemas.Property;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static io.intino.konos.alexandria.activity.Asset.toResource;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ItemBuilder {

    public static Item build(io.intino.konos.alexandria.activity.model.Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        String id = item != null ? item.id() : UUID.randomUUID().toString();

        return new Item().name(new String(Base64.getEncoder().encode(id.getBytes())))
                                .group(group(item, provider.scale()))
                                .label(label(item, provider))
                                .itemBlockList(itemBlockList(item, provider, baseAssetUrl))
                                .itemStampList(itemStampList(item, provider, baseAssetUrl));
    }

    public static List<Item> buildList(List<io.intino.konos.alexandria.activity.model.Item> itemList, ItemBuilderProvider provider, URL baseAssetUrl) {
        return itemList.stream().map(item -> ItemBuilder.build(item, provider, baseAssetUrl)).collect(toList());
    }

    private static String label(io.intino.konos.alexandria.activity.model.Item item, ItemBuilderProvider provider) {
        String defaultLabel = item != null ? item.name() :  "";
        return provider.stamps().stream().filter(s -> (s instanceof Title)).findAny()
                                 .map(stamp -> (String) stamp.value(item, provider.username()))
                                 .orElse(defaultLabel);
    }

    private static Instant group(io.intino.konos.alexandria.activity.model.Item item, TimeScale scale) {
        if (item == null) return null;
        Instant created = item.created();
        return created != null ? scale.normalise(created) : null;
    }

    private static List<ItemBlock> itemBlockList(io.intino.konos.alexandria.activity.model.Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        return provider.blocks().stream().map(block -> itemBlock(item, provider, baseAssetUrl, block)).collect(toList());
    }

    private static ItemBlock itemBlock(io.intino.konos.alexandria.activity.model.Item item, ItemBuilderProvider provider, URL baseAssetUrl, Block block) {
        return new ItemBlock().name(block.name()).hidden(block.hidden(item));
    }

    private static List<ItemStamp> itemStampList(io.intino.konos.alexandria.activity.model.Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        return provider.stamps().stream().map(stamp -> recordItemStamp(item, provider, baseAssetUrl, stamp)).collect(toList());
    }

    private static ItemStamp recordItemStamp(io.intino.konos.alexandria.activity.model.Item item, ItemBuilderProvider provider, URL baseAssetUrl, Stamp stamp) {
        return new ItemStamp().name(stamp.name())
                         .values(valuesOf(stamp, item, provider, baseAssetUrl))
                         .propertyList(propertiesOf(stamp, item, provider, baseAssetUrl));
    }

    private static List<String> valuesOf(Stamp stamp, io.intino.konos.alexandria.activity.model.Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        Object value = stamp.value(item, provider.username());

        if (value instanceof List) {
            List<Object> values = (List<Object>) value;
            if (values.isEmpty() && (stamp instanceof Picture)) values = singletonList(toResource(baseAssetUrl, ((Picture)stamp).defaultPicture()));
            return values.stream().map(v -> valueOf(stamp, v, baseAssetUrl)).collect(toList());
        }

        return singletonList(valueOf(stamp, value, baseAssetUrl));
    }

    private static String valueOf(Stamp stamp, Object value, URL baseAssetUrl) {

        if (stamp instanceof Breadcrumbs) {
            Tree tree = (Tree) value;
            return new Gson().toJson(tree);
        }

        if (stamp instanceof ItemLinks) {
            Links links = (Links) value;
            return new Gson().toJson(links);
        }

        if (stamp instanceof Picture) {
            if (value == null) {
                String defaultPicture = ((Picture) stamp).defaultPicture();
                return defaultPicture != null ? toResource(baseAssetUrl, defaultPicture).toUrl().toString() : "";
            }
            return toResource(baseAssetUrl, (URL)value).toUrl().toString();
        }

        if (stamp instanceof ResourceIcon)
            return value != null ? toResource(baseAssetUrl, (URL) value).toUrl().toString() : "";

        return value != null ? String.valueOf(value) : "";
    }

    private static List<Property> propertiesOf(Stamp stamp, io.intino.konos.alexandria.activity.model.Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        List<Property> result = new ArrayList<>();
        String username = provider.username();
        String style = stamp.style(item, username);

        if (style != null && !style.isEmpty())
            result.add(propertyOf("style", style));

        if (stamp instanceof Highlight)
            result.add(propertyOf("color", ((Highlight)stamp).color(item, username)));

        if (stamp instanceof ItemLinks)
            result.add(propertyOf("title", ((ItemLinks)stamp).title(item, username)));

        if (stamp instanceof CatalogLink)
            result.add(propertyOf("title", ((CatalogLink)stamp).value(item, username)));

        if (stamp instanceof Location) {
            Location location = (Location) stamp;
            URL icon = location.icon(item, username);
            if (icon != null)
                result.add(propertyOf("icon", toResource(baseAssetUrl, icon).toUrl().toString()));
            String drawingColor = location.drawingColor(item, username);
            if (drawingColor != null)
                result.add(propertyOf("drawingColor", drawingColor));
        }

        if (stamp instanceof Icon)
            result.add(propertyOf("title", ((Icon)stamp).title(item, username)));

        return result;
    }

    private static Property propertyOf(String name, String value) {
        return new Property().name(name).value(value);
    }

    public interface ItemBuilderProvider {
        List<Block> blocks();
        List<Stamp> stamps();
        String username();
        TimeScale scale();
    }
}
