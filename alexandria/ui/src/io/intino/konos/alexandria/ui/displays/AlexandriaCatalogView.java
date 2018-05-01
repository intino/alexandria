package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.PictureDataBuilder;
import io.intino.konos.alexandria.ui.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.stamps.Picture;
import io.intino.konos.alexandria.ui.schemas.PictureData;
import io.intino.konos.alexandria.ui.utils.StreamUtil;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class AlexandriaCatalogView<N extends AlexandriaDisplayNotifier> extends AlexandriaElementView<N, CatalogViewDisplayProvider> {

	public AlexandriaCatalogView(Box box) {
		super(box);
	}

	public abstract void reset();
	public abstract List<Item> selectedItems();
	public abstract void refreshSelection(List<String> items);
	protected abstract void refreshPicture(PictureData data);

	void renderExpandedPictures(String item) {
		refreshPictures(item, expandedPictures(item));
	}

	void refreshPictures(String item) {
		refreshPictures(item, allPictures(item));
	}

	private void refreshPictures(String itemId, List<Picture> pictures) {
		Item item = itemOf(itemId);
		pictures.forEach(stamp -> {
			InputStream stream = null;
			try {
				String name = stamp.name();
				Object data = stamp.value(item, session());
				if ((! (data instanceof List)) || ((List) data).size() != 1) return;
				List<URL> values = (List<URL>)data;
				stream = values.get(0).openStream();
				byte[] pictureBytes = IOUtils.toByteArray(stream);
				byte[] picture = Base64.getEncoder().encode(pictureBytes);
				refreshPicture(PictureDataBuilder.build(item, name, "data:image/png;base64," + new String(picture)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				StreamUtil.close(stream);
			}
		});
	}

	private List<Picture> expandedPictures(String item) {
		return provider().expandedStamps(definition().mold()).stream().filter(s -> (s instanceof Picture))
				.map(s -> (Picture)s)
				.collect(toList());
	}

	private List<Picture> allPictures(String item) {
		return provider().stamps(definition().mold()).stream().filter(s -> (s instanceof Picture))
				.map(s -> (Picture)s)
				.collect(toList());
	}

}
