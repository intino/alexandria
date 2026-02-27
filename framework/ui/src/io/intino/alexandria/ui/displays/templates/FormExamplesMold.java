package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Resource;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.components.ImageEditable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FormExamplesMold extends AbstractFormExamplesMold<UiFrameworkBox> {
	private Resource r1;
	private Resource r2;

	public FormExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		editableText1.onChange(e -> text1.value(e.value()));
		editableText2.onChange(e -> text2.value(e.value()));
		editableText3.onChange(e -> text3.value(e.value()));
		editableText4.onChange(e -> text4.value(e.value()));
		editableText5.onChange(e -> text5.value(e.value()));
		editableText6.onChange(e -> text6.value(e.value()));
		text7.error("Lorem ipsum dolor sit amet");
		editableText7.onChange(e -> text7.value(e.value()));
		editableText7.error("Lorem ipsum dolor sit amet");
		editableText8.onChange(e -> text8.value(e.value()));
		editableMemo1.onChange(e -> memo1.value(e.value()));
		editableMemo2.onChange(e -> memo2.value(e.value()));
		editableNumber1.onChange(e -> number1.value(e.value()));
		editableNumber2.onChange(e -> number2.value(e.value()));
		number3.error("Lorem ipsum dolor sit amet");
		editableNumber3.onChange(e -> number3.value(e.value()));
		editableNumber3.error("Lorem ipsum dolor sit amet");
		editableDate1.onChange(e -> date1.value(e.value()));
		editableDate2.onChange(e -> date2.value(e.value()));
		editableDate3.onChange(e -> date3.value(e.value()));
//		editableDate4.onChange(e -> date4.value(e.value()));
		editableImage1.onChange(e -> update(e, image1, editableImage1));
		editableImage2.onChange(e -> update(e, image2, editableImage2));
		editableImage3.onChange(e -> update(e, image3, editableImage3));
		editableImage4.onChange(e -> update(e, image4, editableImage4));
		editableFile1.maxSize(100);
		editableSelector1.onSelect(e -> {
			if (e.selection().isEmpty()) selector1.select();
			else selector1.select(firstLowerCase(((String) e.selection().get(0)).replace("editable", "")));
		});
		editableSelector2.onSelect(e -> {
			if (e.selection().isEmpty()) selector2.select();
			else selector2.select(e.selection().stream().map(v -> firstLowerCase(((String) v).replace("editable", ""))).toArray(String[]::new));
		});
	}

	private void update(ChangeEvent event, ImageEditable<?, ?> editable, ImageEditable<?, ?> readonly) {
		try {
			Resource value = event.value();
			update(value, editable);
			if (value != null) value.stream().reset();
			update(value, readonly);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private String firstLowerCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	private void update(Resource value, ImageEditable<?, ?> display) {
		try {
			java.io.File file = value != null ? toFile("/tmp/updated-test-image.png", value) : null;
			display.value(file != null ? file.toURI().toURL() : null);
		} catch (IOException e) {
			Logger.error(e);
		}

	}

	private static java.io.File toFile(String path, Resource resource) throws IOException {
		java.io.File file = new java.io.File(path);
		FileOutputStream stream = new FileOutputStream(file, false);
		stream.write(toByteArray(resource));
		stream.close();
		return file;
	}

	private static byte[] toByteArray(Resource resource) throws IOException {
		InputStream stream = resource.stream();
		byte[] targetArray = new byte[stream.available()];
		stream.read(targetArray);
		return targetArray;
	}

}