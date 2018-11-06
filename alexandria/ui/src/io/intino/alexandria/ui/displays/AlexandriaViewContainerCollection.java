package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.builders.PictureDataBuilder;
import io.intino.alexandria.ui.displays.providers.CatalogViewDisplayProvider;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.model.mold.stamps.Picture;
import io.intino.alexandria.ui.schemas.PictureData;
import io.intino.alexandria.ui.utils.StreamUtil;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class AlexandriaViewContainerCollection<N extends AlexandriaDisplayNotifier> extends AlexandriaElementView<N, CatalogViewDisplayProvider> {

	public AlexandriaViewContainerCollection(Box box) {
		super(box);
	}

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
				URL url = values.get(0);
				if (url == null) return;
				stream = url.openStream();
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
		return provider().expandedStamps(view().mold()).stream().filter(s -> (s instanceof Picture))
				.map(s -> (Picture)s)
				.collect(toList());
	}

	private List<Picture> allPictures(String item) {
		return provider().stamps(view().mold()).stream().filter(s -> (s instanceof Picture))
				.map(s -> (Picture)s)
				.collect(toList());
	}

}
