package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.accessor.ui.android.templates.ThemeTemplate;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Format;
import io.intino.konos.model.Service;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class ThemeRenderer extends UIRenderer {
	private final Service.UI service;
	private final Map<String, Format> formatMap;
	private final Set<String> usedFormats;

	protected ThemeRenderer(CompilationContext compilationContext, Service.UI service, Set<String> usedFormats) {
		super(compilationContext);
		this.service = service;
		this.formatMap = service.graph().formatList().stream().collect(toMap(Layer::name$, f -> f));
		this.usedFormats = usedFormats;
	}

	@Override
	public void render() {
		renderFormats();
		renderColors();
	}

	private void renderFormats() {
		FrameBuilder builder = new FrameBuilder("theme");
		usedFormats.stream().filter(u -> !u.isEmpty()).forEach(f -> builder.add("format", formatFrameOf(f)));
		Commons.write(new File(res(Target.AndroidResource) + File.separator + "values" + File.separator + "styles.xml").toPath(), setup(new ThemeTemplate()).render(builder.toFrame()));
	}

	private void renderColors() {
		FrameBuilder builder = new FrameBuilder("colors");
		Commons.write(new File(res(Target.AndroidResource) + File.separator + "values" + File.separator + "colors.xml").toPath(), setup(new ThemeTemplate()).render(builder.toFrame()));
	}

	private Frame formatFrameOf(String usedFormat) {
		FrameBuilder result = new FrameBuilder("format");
		result.add("name", usedFormat.replace("-", "_"));
		String[] usedFormats = usedFormat.split("-");
		String attributes = Arrays.stream(usedFormats).map(f -> formatMap.get(f).content()).filter(c -> c != null && !c.isEmpty()).collect(Collectors.joining(","));
		if (attributes.isEmpty()) return result.toFrame();
		Arrays.stream(attributes.split(",")).filter(this::validAttribute).forEach(a -> result.add("attribute", attributeFrameOf(a)));
		return result.toFrame();
	}

	private static final Set<String> InvalidAttributes = Set.of(
			"border", "borderTop", "borderBottom", "borderRight", "borderLeft", "borderRadius",
			"textShadow", "whiteSpace", "overflow"
	);
	private boolean validAttribute(String attribute) {
		return !InvalidAttributes.contains(attribute.split(":")[0]);
	}

	private Frame attributeFrameOf(String attribute) {
		String[] props = attribute.split(":");
		String value = props[1].substring(1, props[1].length() - 1);
		FrameBuilder result = new FrameBuilder("attribute", props[0]);
		result.add("name", adaptName(props[0]));
		result.add("value", adaptValue(props[0], value));
		adaptSizeable(props[0], value, result);
		return result.toFrame();
	}

	private static final Set<String> SizeableAttributes = Set.of("margin", "padding");
	private void adaptSizeable(String attribute, String value, FrameBuilder result) {
		if (!SizeableAttributes.contains(attribute)) return;
		String[] parts = value.split(" ");
		result.add("sizeable");
		result.add("topName", adaptName(attribute + "Top"));
		result.add("top", adaptValue(attribute, parts[0]));
		result.add("rightName", adaptName(attribute + "Right"));
		result.add("right", adaptValue(attribute, parts.length > 1 ? parts[1] : parts[0]));
		result.add("bottomName", adaptName(attribute + "Bottom"));
		result.add("bottom", adaptValue(attribute, parts.length > 2 ? parts[2] : parts[0]));
		result.add("leftName", adaptName(attribute + "Left"));
		result.add("left", adaptValue(attribute, parts.length == 4 ? parts[3] : parts.length > 1 ? parts[1] : parts[0]));
	}

	private static final Map<String, String> AdapterMap = Map.of(
		"color", "textColor",
		"fontSize", "textSize",
		"marginTop", "layout_marginTop",
		"marginRight", "layout_marginRight",
		"marginBottom", "layout_marginBottom",
		"marginLeft", "layout_marginLeft",
		"textAlign", "textAlignment",
		"fontWeight", "textFontWeight"
	);
	private String adaptName(String attribute) {
		return AdapterMap.getOrDefault(attribute, attribute);
	}

	private String adaptValue(String attribute, String value) {
		if (value.equals("0")) return "0px";
		if (value.equals("100%")) return "100sp";
		if (attribute.equals("textAlign") && value.equals("left")) return "textStart";
		if (attribute.equals("textAlign") && value.equals("right")) return "textEnd";
		if (attribute.equals("fontWeight") && value.equals("bold")) return "700";
		if (attribute.equals("color") && !value.startsWith("#")) return "@color/" + value;
		if (attribute.equals("background") && !value.startsWith("#")) return "@color/" + value;
		return value;
	}
}
