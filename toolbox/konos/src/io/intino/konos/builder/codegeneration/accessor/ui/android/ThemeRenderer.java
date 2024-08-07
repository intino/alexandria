package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.accessor.ui.android.templates.ThemeTemplate;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Format;
import io.intino.konos.dsl.Service;
import io.intino.konos.dsl.Theme;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.*;
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
		Theme theme = service.graph().theme();
		FrameBuilder builder = new FrameBuilder("theme");
		builder.add("type", new FrameBuilder("type", theme != null ? theme.type().name() : Theme.Type.Light.name()));
		usedFormats.stream().filter(u -> !u.isEmpty()).forEach(f -> builder.add("format", formatFrameOf(f)));
		Commons.write(new File(res(Target.AndroidResource) + File.separator + "values" + File.separator + "styles.xml").toPath(), new ThemeTemplate().render(builder.toFrame(), Formatters.all));
	}

	private void renderColors() {
		FrameBuilder builder = new FrameBuilder("colors");
		Commons.write(new File(res(Target.AndroidResource) + File.separator + "values" + File.separator + "colors.xml").toPath(), new ThemeTemplate().render(builder.toFrame(), Formatters.all));
	}

	private Frame formatFrameOf(String usedFormat) {
		FrameBuilder result = new FrameBuilder("format");
		result.add("name", usedFormat.replace("-", "_"));
		String[] usedFormats = usedFormat.split("-");
		String attributes = Arrays.stream(usedFormats).map(f -> formatMap.get(f).content()).filter(c -> c != null && !c.isEmpty()).collect(Collectors.joining(","));
		addDefaultAttributes(result, usedFormats);
		if (attributes.isEmpty()) return result.toFrame();
		Arrays.stream(attributes.split(",")).filter(this::validAttribute).forEach(a -> result.add("attribute", attributeFrameOf(a)));
		return result.toFrame();
	}

	private void addDefaultAttributes(FrameBuilder result, String[] usedFormats) {
		List<String> defaultTextSizes = Arrays.stream(usedFormats).map(this::defaultTextSize).filter(Objects::nonNull).toList();
		if (!defaultTextSizes.isEmpty()) result.add("defaultTextSize", defaultTextSizes.get(0));
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

	private String defaultTextSize(String attribute) {
		if (attribute.equalsIgnoreCase("h1")) return "48sp";
		if (attribute.equalsIgnoreCase("h2")) return "38sp";
		if (attribute.equalsIgnoreCase("h3")) return "30sp";
		if (attribute.equalsIgnoreCase("h4")) return "28sp";
		if (attribute.equalsIgnoreCase("h5")) return "24sp";
		if (attribute.equalsIgnoreCase("h6")) return "20sp";
		if (attribute.equalsIgnoreCase("body1")) return "12sp";
		if (attribute.equalsIgnoreCase("body2")) return "8sp";
		return null;
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
		if (value.contains("px")) return pixelsToDensityPixels(value);
		return value;
	}

	private String pixelsToDensityPixels(String value) {
		String[] parts = value.split(" ");
		StringBuilder result = new StringBuilder();
		for (String part : parts) {
			if (!result.isEmpty()) result.append(" ");
			result.append(part.contains("px") ? Math.round(Integer.parseInt(part.replace("px", ""))) + "px" : part);
		}
		return result.toString();
	}
}
