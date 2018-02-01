package io.intino.konos.alexandria.activity.displays.builders;

import io.intino.konos.alexandria.activity.model.mold.Block;
import io.intino.konos.alexandria.activity.model.mold.stamps.*;
import io.intino.konos.alexandria.activity.model.mold.stamps.icons.AlexandriaIcon;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.*;
import io.intino.konos.alexandria.activity.schemas.Mold;
import io.intino.konos.alexandria.activity.schemas.MoldBlock;
import io.intino.konos.alexandria.activity.schemas.Property;
import io.intino.konos.alexandria.activity.schemas.Stamp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MoldBuilder {

    public static Mold build(io.intino.konos.alexandria.activity.model.Mold mold) {
        return new Mold().moldBlockList(mold.blocks().stream().map(MoldBuilder::buildBlock).collect(toList()));
    }

    private static MoldBlock buildBlock(Block block) {
        return new MoldBlock().name(block.name()).style(block.style()).expanded(block.expanded())
                              .hiddenIfMobile(block.hiddenIfMobile())
                              .layout(block.layouts().stream().map(Enum::toString).collect(Collectors.joining(" ")))
                              .width(block.width())
                              .height(block.height())
                              .moldBlockList(block.blockList().stream().map(MoldBuilder::buildBlock).collect(toList()))
                              .stampList(block.stampList().stream().map(MoldBuilder::buildStamp).collect(toList()));
    }

    private static Stamp buildStamp(io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        Stamp result = new Stamp().name(stamp.name()).label(stamp.label())
                                  .editable(stamp.editable())
                                  .shape(shapeOf(stamp)).layout(stamp.layout().toString())
                                  .height(stamp.height());

        List<Property> propertyList = new ArrayList<>();
        addCommonProperties(propertyList, stamp);
        addRatingProperties(propertyList, stamp);
        addEmbeddedDisplayProperties(propertyList, stamp);
        addEmbeddedDialogProperties(propertyList, stamp);
        addEmbeddedCatalogProperties(propertyList, stamp);
        addIconProperties(propertyList, stamp);
        addDownloadOperationProperties(propertyList, stamp);
        addPreviewOperationProperties(propertyList, stamp);
        addExportOperationProperties(propertyList, stamp);
        addMapProperties(propertyList, stamp);
        result.propertyList(propertyList);

        return result;
    }

    private static String shapeOf(io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (stamp instanceof Title) return "title";
        if (stamp instanceof Description) return "description";
        if (stamp instanceof Icon) return "icon";
        if (stamp instanceof Rating) return "rating";
        if (stamp instanceof Highlight) return "highlight";
        if (stamp instanceof Picture) return "picture";
        if (stamp instanceof OpenDialogOperation) return "open-dialog-operation";
        if (stamp instanceof DownloadOperation) return "download-operation";
        if (stamp instanceof PreviewOperation) return "preview-operation";
        if (stamp instanceof ExportOperation) return "export-operation";
        if (stamp instanceof TaskOperation) return "task-operation";
        if (stamp instanceof Page) return "page";
        if (stamp instanceof Location) return "location";
        if (stamp instanceof Breadcrumbs) return "breadcrumbs";
        if (stamp instanceof ItemLinks) return "item-links";
        if (stamp instanceof CatalogLink) return "catalog-link";
        if (stamp instanceof Snippet) return "snippet";
        if (stamp instanceof CardWallet) return "card-wallet";
        if (stamp instanceof EmbeddedDisplay) return "embedded-display";
        if (stamp instanceof EmbeddedDialog) return "embedded-dialog";
        if (stamp instanceof EmbeddedCatalog) return "embedded-catalog";
        if (stamp instanceof Map) return "map";
        return "";
    }

    private static void addCommonProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (!stamp.suffix().isEmpty()) propertyList.add(shapeProperty("suffix", stamp.suffix()));
        if (!stamp.defaultStyle().isEmpty()) propertyList.add(shapeProperty("defaultStyle", stamp.defaultStyle()));
    }

    private static void addRatingProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (! (stamp instanceof Rating)) return;
        propertyList.add(shapeProperty("icon", ((Rating)stamp).ratingIcon()));
    }

    private static void addEmbeddedDisplayProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (! (stamp instanceof EmbeddedDisplay)) return;
        propertyList.add(shapeProperty("displayType", ((EmbeddedDisplay)stamp).displayType()));
    }

    private static void addEmbeddedDialogProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (! (stamp instanceof EmbeddedDialog)) return;
        propertyList.add(shapeProperty("dialogType", ((EmbeddedDialog)stamp).dialogType()));
    }

    private static void addEmbeddedCatalogProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (! (stamp instanceof EmbeddedCatalog)) return;
        propertyList.add(shapeProperty("catalog", ((EmbeddedCatalog)stamp).catalog().name()));
    }

    private static void addIconProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (! (stamp instanceof Icon)) return;
        propertyList.add(shapeProperty("icon-type", stamp instanceof AlexandriaIcon ? "alexandria" : ""));
    }

    private static void addDownloadOperationProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (! (stamp instanceof DownloadOperation)) return;
        DownloadOperation downloadStamp = (DownloadOperation)stamp;
        propertyList.add(shapeProperty("title", downloadStamp.label()));
        propertyList.add(shapeProperty("options", String.join(",", downloadStamp.options())));
    }

    private static void addPreviewOperationProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (! (stamp instanceof PreviewOperation)) return;
        PreviewOperation previewStamp = (PreviewOperation)stamp;
        propertyList.add(shapeProperty("title", previewStamp.label()));
    }

    private static void addExportOperationProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (! (stamp instanceof ExportOperation)) return;
        ExportOperation exportStamp = (ExportOperation)stamp;
        propertyList.add(shapeProperty("title", exportStamp.label()));
        propertyList.add(shapeProperty("options", String.join(",", exportStamp.options())));
        propertyList.add(shapeProperty("from", String.valueOf(exportStamp.from().toEpochMilli())));
        propertyList.add(shapeProperty("to", String.valueOf(exportStamp.to().toEpochMilli())));
    }

    private static void addMapProperties(List<Property> propertyList, io.intino.konos.alexandria.activity.model.mold.Stamp stamp) {
        if (! (stamp instanceof Map)) return;
        Map mapStamp = (Map)stamp;
        propertyList.add(shapeProperty("zoom", String.valueOf(mapStamp.zoom())));
        propertyList.add(shapeProperty("latitude", String.valueOf(mapStamp.latitude())));
        propertyList.add(shapeProperty("longitude", String.valueOf(mapStamp.longitude())));
    }

    private static Property shapeProperty(String name, String value) {
        return new Property().name(name).value(value);
    }

}
