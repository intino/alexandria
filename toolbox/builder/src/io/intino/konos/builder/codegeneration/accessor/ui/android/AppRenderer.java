package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.accessor.ui.android.templates.AppTemplate;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppRenderer extends UIRenderer {
	private final Service.UI service;

	protected AppRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext);
		this.service = service;
	}

	@Override
	public void render() {
		writeManifest(buildFrame().add("manifest"));
		writeSettingsGradle(buildFrame().add("settings"));
		writeAndroidGradle(buildFrame().add("android"));
		writeSharedGradle(buildFrame().add("shared"));
		writeProperties(buildFrame().add("properties"));
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder result = super.buildFrame();
		result.add("gradle");
		result.add("project", context.project());
		service.resourceList().stream().filter(Service.UI.Resource::isPage).forEach(r -> result.add("resource", resourceFrame(r)));
		return result;
	}

	private FrameBuilder resourceFrame(Service.UI.Resource resource) {
		FrameBuilder result = buildBaseFrame().add("resource");
		if (resource.isMain()) result.add("main");
		result.add("name", resource.name$());
		return result;
	}

	private void writeManifest(FrameBuilder builder) {
		Commons.write(new File(root(Target.Android) + context.androidRelativePath() + File.separator + "AndroidManifest.xml").toPath(), setup(new AppTemplate()).render(builder.toFrame()));
	}

	private void writeSettingsGradle(FrameBuilder builder) {
		Commons.write(new File(root(Target.Android) + File.separator + "settings.gradle.kts").toPath(), setup(new AppTemplate()).render(builder.toFrame()));
	}

	private void writeAndroidGradle(FrameBuilder builder) {
		Commons.write(new File(root(Target.Android) + File.separator + "android" + File.separator + "build.gradle.kts").toPath(), setup(new AppTemplate()).render(builder.toFrame()));
	}

	private void writeSharedGradle(FrameBuilder builder) {
		Commons.write(new File(root(Target.Android) + File.separator + "shared" + File.separator + "build.gradle.kts").toPath(), setup(new AppTemplate()).render(builder.toFrame()));
	}

	private void writeProperties(FrameBuilder builder) {
		addProperties(builder);
		Commons.write(new File(res(Target.Android) + File.separator + "values" + File.separator + "properties.xml").toPath(), setup(new AppTemplate()).render(builder.toFrame()));
	}

	private static final Map<String, List<String>> ComponentPropertiesMap = new HashMap<>() {{
		put("Alexandria", List.of("name", "label", "format", "color", "visible", "traceable", "style", "context", "owner", "multiple_instances", "multiple_arrangement", "multiple_no_items_message", "multiple_spacing", "multiple_editable", "multiple_wrap", "multiple_collapsed", "multiple_count_min", "multiple_count_max", "highlighted", "highlighted_background"));
		put("Actionable:Alexandria", List.of("title", "target", "mode", "icon", "affirmed", "traceable", "signed", "size", "highlighted", "readonly"));
		put("AbstractSlider:Alexandria", List.of("traceable", "arrangement", "animation", "readonly", "position", "style"));
		put("AppDirectory:Alexandria", List.of("icon"));
		put("Dashboard:Alexandria", List.of("width", "height"));
		put("OpenPopover:Alexandria", List.of("trigger_event"));
		put("SignText:Alexandria", List.of("content", "format"));
		put("Template:Alexandria", List.of("layout", "width", "height", "spacing"));
		put("BaseStamp:Alexandria", List.of("spacing"));
		put("MaterialIcon:Alexandria", List.of("icon"));
		put("Selector:Alexandria", List.of("multiple_selection", "readonly", "focused", "placeholder", "selected", "layout", "size", "max_menu_height", "allow_other", "scroll_buttons", "view"));
		put("Image:Alexandria", List.of("width", "height", "mobile_reduce_factor", "allow_full_screen"));
		put("File:Alexandria", List.of("width", "height", "preview", "drop_zone", "max_size", "allowed_types"));
		put("Chart:Alexandria", List.of("width", "height"));
		put("AlertDialog:Alexandria", List.of("title", "modal", "full_screen", "message", "close_label", "accept_label", "width", "height", "animation"));
		put("AbstractDialog:Alexandria", List.of("title", "modal", "full_screen", "width", "height", "animation"));
		put("Block:Alexandria", List.of("layout", "width", "height", "animation", "hidden", "auto_size", "paper"));
		put("Date:Alexandria", List.of("pattern", "mode", "value", "time_picker", "mask", "embedded", "allow_empty", "views", "shrink"));
		put("User:Alexandria", List.of("mode"));
		put("Number:Alexandria", List.of("value", "prefix", "suffix", "min", "max", "step", "readonly", "focused", "decimals", "expanded", "helper_text", "shrink"));
		put("Header:Alexandria", List.of("position", "width", "height", "elevation"));
		put("Code:Alexandria", List.of("mode", "language", "highlighted"));
		put("Text:Alexandria", List.of("mode", "edition_mode", "max_length", "rows", "prefix", "suffix", "translate", "crop_with_ellipsis", "value", "placeholder", "readonly", "focused", "highlighted", "type", "helper_text", "shrink"));
		put("Location:Alexandria", List.of("center", "zoom", "modes", "controls"));
		put("Collection:Alexandria", List.of("no_items_message", "no_items_found_message", "page_size", "item_height", "scrolling_mark", "navigable", "selection"));
		put("Map:Collection", List.of("page_size", "type", "item_height", "center", "zoom", "controls"));
		put("Heading:Alexandria", List.of("style", "hidden"));
		put("Item:Alexandria", List.of("style", "hidden"));
		put("Spinner:Alexandria", List.of("mode", "size"));
		put("Switch:Alexandria", List.of("state"));
		put("Toggle:Alexandria", List.of("state"));
		put("SplitButton:Alexandria", List.of("options", "default_option"));
		put("Export:Alexandria", List.of("from", "to", "min", "max", "range", "options"));
		put("Download:Alexandria", List.of("options"));
		put("SearchBox:Alexandria", List.of("placeholder", "show_count_message"));
		put("Slider:Alexandria", List.of("range", "value"));
		put("Grouping:Alexandria", List.of("page_size", "placeholder"));
		put("Stepper:Alexandria", List.of("orientation", "position"));
		put("Frame:Alexandria", List.of("width", "height", "url"));
		put("Sorting:Alexandria", List.of("mode", "align"));
		put("HtmlViewer:Alexandria", List.of("content"));
		put("Microsite:Alexandria", List.of("download_operations"));
	}};
	private void addProperties(FrameBuilder builder) {
		ComponentPropertiesMap.forEach((c, attributes) -> builder.add("component", componentFrameOf(c, attributes)));
	}

	private FrameBuilder componentFrameOf(String component, List<String> attributes) {
		FrameBuilder result = new FrameBuilder("component");
		String name = component.split(":")[0];
		String parent = component.split(":").length > 1 ? component.split(":")[1] : null;
		result.add("name", name);
		if (parent != null) result.add("parent", context.project());
		attributes.forEach(a -> result.add("attribute", attributeFrame(name, parent, a)));
		return result;
	}

	private FrameBuilder attributeFrame(String componentName, String componentParent, String attribute) {
		FrameBuilder result = new FrameBuilder("attribute");
		//if (componentParent == null) result.add("root");
		result.add("name", attribute);
		result.add("component", componentName);
		return result;
	}

}
