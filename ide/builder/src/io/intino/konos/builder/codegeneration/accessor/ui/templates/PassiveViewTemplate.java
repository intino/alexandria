package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class PassiveViewTemplate extends Template {

	protected PassiveViewTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PassiveViewTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "notifier"))).add(literal("export default class Notifier {\n\tconstructor(element) {\n        this.element = element;\n        this.pushService = Application.services.pushService;\n        this.pushRegistrations = [];\n\t\tthis.pushLinked = false;\n    };\n\n\tsetup() {\n\t\tthis.when(\"add\").toSelf().execute((parameters) => this.element.add(parameters));\n\t\tthis.when(\"remove\").toSelf().execute((parameters) => this.element.remove(parameters));\n\t};\n\n\twhen = (message) => {\n\t\tvar register = _register.bind(this);\n\t\tvar element = this.element;\n\t\tvar pushService = this.pushService;\n\t\treturn {\n\t\t\ttoSelf: function () {\n\t\t\t\treturn {\n\t\t\t\t\texecute: function (action) {\n\t\t\t\t\t\tregister(pushService.listen(message, function (parameters) {\n\t\t\t\t\t\t\tlet id = parameters.i;\n\t\t\t\t\t\t\tlet owner = parameters.o;\n\t\t\t\t\t\t\tif (id === element.props.id && (owner == null || owner === \"\" || owner === element.props.owner)) {\n\t\t\t\t\t\t\t\taction(parameters);\n\t\t\t\t\t\t\t}\n\t\t\t\t\t\t}));\n\t\t\t\t\t}\n\t\t\t\t}\n\t\t\t},\n\t\t\texecute: function (action) {\n\t\t\t\tregister(pushService.listen(message, function(parameters) {\n\t\t\t\t\tif (parameters.n === element.name) {\n\t\t\t\t\t\taction(parameters);\n\t\t\t\t\t}\n\t\t\t\t}));\n\t\t\t}\n\t\t};\n\n\t\tfunction _register(registration) {\n\t\t\tthis.pushRegistrations.push(registration);\n\t\t}\n\t};\n\n\tdetached = () => {\n\t\tthis.pushRegistrations.forEach((registrations) => registrations.deregister());\n\t\tthis.pushRegistrations.splice(0, this.pushRegistrations.length);\n\t};\n}")),
			rule().add((condition("type", "requester"))).add(literal("export default class Requester {\n\tconstructor(element) {\n\t\tthis.element = element;\n\t\tthis.pushService = Application.services.pushService;\n\t\tthis.fileService = Application.services.fileService;\n\t};\n}"))
		);
		return this;
	}
}