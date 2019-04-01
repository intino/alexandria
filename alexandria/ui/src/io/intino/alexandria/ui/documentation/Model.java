package io.intino.alexandria.ui.documentation;

import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.MethodParameter;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.documentation.model.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
	private static Map<WidgetType, Widget> map = new HashMap<>();

	public enum WidgetType {
		Text, Number, Image, File, Date, Chart
	}

	static {
		initialize();
	}

	private static void initialize() {
		map.put(WidgetType.Text, new TextWidget());
		map.put(WidgetType.Number, new NumberWidget());
		map.put(WidgetType.Image, new ImageWidget());
		map.put(WidgetType.File, new FileWidget());
		map.put(WidgetType.Date, new DateWidget());
		map.put(WidgetType.Chart, new ChartWidget());
	}

	public static Widget widget(WidgetType type) {
		if (!map.containsKey(type)) return new Widget();
		return map.get(type);
	}

	public static Property property(String name, Property.Type type, String description, String... values) {
		Property result = new Property().name(name).type(type).description(description);
		if (values.length > 0) result.values(Arrays.asList(values));
		return result;
	}

	public static Method method(String name, List<MethodParameter> params, String description, String returnType) {
		return new Method().name(name).params(params).description(description).returnType(returnType);
	}

	public static MethodParameter methodParameter(String name, String type) {
		return new MethodParameter().name(name).type(type);
	}

}
