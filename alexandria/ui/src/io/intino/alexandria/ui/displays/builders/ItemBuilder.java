package io.intino.alexandria.ui.displays.builders;

import com.google.gson.Gson;
import io.intino.alexandria.ui.Asset;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.model.TimeScale;
import io.intino.alexandria.ui.model.mold.Block;
import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.model.mold.stamps.*;
import io.intino.alexandria.ui.model.mold.stamps.icons.ResourceIcon;
import io.intino.alexandria.ui.model.mold.stamps.operations.OpenExternalDialogOperation;
import io.intino.alexandria.ui.model.mold.stamps.operations.PreviewOperation;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.utils.AvatarUtil;
import io.intino.konos.alexandria.ui.schemas.Item;
import io.intino.konos.alexandria.ui.schemas.ItemBlock;
import io.intino.konos.alexandria.ui.schemas.ItemStamp;
import io.intino.konos.alexandria.ui.schemas.Property;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static io.intino.alexandria.ui.Asset.toResource;
import static io.intino.alexandria.ui.utils.AvatarUtil.generateAvatar;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ItemBuilder {

    public static Item build(Item item, String id, ItemBuilderProvider provider, URL baseAssetUrl) {
        return new Item().name(new String(Base64.getEncoder().encode(id.getBytes())))
                .group(group(item, provider.scale()))
                .label(label(item, provider))
                .itemBlockList(itemBlockList(item, provider, baseAssetUrl))
                .itemStampList(itemStampList(item, provider, baseAssetUrl));
    }

    public static Item buildOnlyLocation(Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        try {
            String id = item != null ? item.id() : UUID.randomUUID().toString();

            return new Item().name(new String(Base64.getEncoder().encode(id.getBytes())))
                    .group(group(item, provider.scale()))
                    .label(label(item, provider))
                    .itemBlockList(emptyList())
                    .itemStampList(itemLocationStampList(item, provider, baseAssetUrl));
        }
        catch (Throwable exception) {
            Logger.getGlobal().severe(exception.getMessage());
            return null;
        }
    }

    public static List<Item> buildList(List<Item> itemList, ItemBuilderProvider provider, URL baseAssetUrl) {
        return itemList.stream().map(item -> ItemBuilder.build(item, item.id(), provider, baseAssetUrl)).collect(toList());
    }

    public static List<Item> buildListOnlyLocation(List<Item> itemList, ItemBuilderProvider provider, URL baseAssetUrl) {
        return itemList.stream().map(item -> ItemBuilder.buildOnlyLocation(item, provider, baseAssetUrl)).collect(toList());
    }

    private static String label(Item item, ItemBuilderProvider provider) {
        String defaultLabel = item != null ? item.name() : "";
        return provider.stamps().stream().filter(s -> (s instanceof Title)).findAny()
                                 .map(stamp -> (String) stamp.value(item, provider.session()))
                                 .orElse(defaultLabel);
    }

    private static Instant group(Item item, TimeScale scale) {
        if (item == null) return null;
        Instant created = item.created();
        return created != null ? scale.normalise(created) : null;
    }

    private static List<ItemBlock> itemBlockList(Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        return provider.blocks().stream().map(block -> itemBlock(item, provider, baseAssetUrl, block)).collect(toList());
    }

    private static ItemBlock itemBlock(Item item, ItemBuilderProvider provider, URL baseAssetUrl, Block block) {
        return new ItemBlock().name(block.name()).hidden(block.hidden(item, provider.session())).className(block.className(item, provider.session()));
    }

    private static List<ItemStamp> itemStampList(Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        return provider.stamps().stream().map(stamp -> recordItemStamp(item, provider, baseAssetUrl, stamp)).collect(toList());
    }

    private static List<ItemStamp> itemLocationStampList(Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        return provider.stamps().stream().filter(stamp-> stamp instanceof Location).map(stamp -> recordItemStamp(item, provider, baseAssetUrl, stamp)).collect(toList());
    }

    private static ItemStamp recordItemStamp(Item item, ItemBuilderProvider provider, URL baseAssetUrl, Stamp stamp) {
        return new ItemStamp().name(stamp.name())
                         .values(valuesOf(stamp, item, provider, baseAssetUrl))
                         .className(stamp.className(item, provider.session()))
                         .propertyList(propertiesOf(stamp, item, provider, baseAssetUrl));
    }

    private static List<String> valuesOf(Stamp stamp, Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        Object value = stamp.value(item, provider.session());

        if (value instanceof List) {
            List<Object> values = (List<Object>) value;
            if (values.isEmpty() && (stamp instanceof Picture)) values = singletonList(Asset.toResource(baseAssetUrl, ((Picture)stamp).defaultPicture()));
            return values.stream().map(v -> valueOf(stamp, item, v, provider, baseAssetUrl)).collect(toList());
        }

        return singletonList(valueOf(stamp, item, value, provider, baseAssetUrl));
    }

    private static String valueOf(Stamp stamp, Item item, Object value, ItemBuilderProvider provider, URL baseAssetUrl) {

        if (stamp instanceof Breadcrumbs) {
            Tree tree = (Tree) value;
            return tree != null ? new Gson().toJson(tree) : "";
        }

        if (stamp instanceof CardWallet) {
            Wallet wallet = (Wallet) value;
            return wallet != null ? new Gson().toJson(wallet) : "";
        }

        if (stamp instanceof Pie || stamp instanceof Timeline || stamp instanceof Histogram) {
            ChartData data = (ChartData) value;
            return data != null ? new Gson().toJson(data) : "";
        }

        if (stamp instanceof ItemLinks) {
            Links links = (Links) value;
            return links != null ? new Gson().toJson(links) : "";
        }

        if (stamp instanceof Picture) {
            if (value == null) {
                Picture picture = (Picture) stamp;
                String defaultPicture = picture.defaultPicture();

                if (defaultPicture == null && picture.isAvatar()) {
                    Picture.AvatarProperties properties = picture.avatarProperties(item, provider.session());
                    return AvatarUtil.generateAvatar(properties.text(), properties.color());
                }

                return defaultPicture != null ? Asset.toResource(baseAssetUrl, defaultPicture).toUrl().toString() : "";
            }
            return Asset.toResource(baseAssetUrl, (URL)value).toUrl().toString();
        }

        if (stamp instanceof ResourceIcon)
            return value != null ? Asset.toResource(baseAssetUrl, (URL) value).toUrl().toString() : "";

        return value != null ? String.valueOf(value) : "";
    }

    private static List<Property> propertiesOf(Stamp stamp, Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
        List<Property> result = new ArrayList<>();
        UISession session = provider.session();

        String style = stamp.style(item, session);
        if (style != null && !style.isEmpty()) result.add(propertyOf("style", style));

        String className = stamp.className(item, session);
        if (className != null && !className.isEmpty()) result.add(propertyOf("className", className));

        Stamp.Color color = stamp.color(item, session);
        if (color != null) result.add(propertyOf("color", new Gson().toJson(color)));

        String label = stamp.label(item, session);
        if (label != null && !label.isEmpty()) result.add(propertyOf("label", label));

        if (stamp instanceof ItemLinks)
            result.add(propertyOf("title", ((ItemLinks)stamp).title(item, session)));

        if (stamp instanceof CatalogLink)
            result.add(propertyOf("title", ((CatalogLink)stamp).value(item, session)));

        if (stamp instanceof Location) {
            Location location = (Location) stamp;
            URL icon = location.icon(item, session);
            if (icon != null) result.add(propertyOf("icon", Asset.toResource(baseAssetUrl, icon).toUrl().toString()));
        }

        if (stamp instanceof OpenExternalDialogOperation) {
            result.add(propertyOf("dialogTitle", ((OpenExternalDialogOperation) stamp).dialogTitle(item, session)));
            result.add(propertyOf("dialogPath", ((OpenExternalDialogOperation) stamp).dialogPath(item, session)));
        }

        if (stamp instanceof PreviewOperation) {
            URL preview = ((PreviewOperation)stamp).preview(item, session);
            if (preview != null)
                result.add(propertyOf("document", Asset.toResource(baseAssetUrl, preview).setEmbedded(true).toUrl().toString()));
        }

        if (stamp instanceof Icon)
            result.add(propertyOf("title", ((Icon)stamp).title(item, session)));

        return result;
    }

    private static Property propertyOf(String name, String value) {
        return new Property().name(name).value(value);
    }

    public interface ItemBuilderProvider {
        List<Block> blocks();
        List<Stamp> stamps();
        UISession session();
        TimeScale scale();
    }
}
