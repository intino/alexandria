package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class PassiveViewTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("notifier"))).output(literal("export default class Notifier {\n\tstatic subscriptions = [];\n\n\tconstructor(element) {\n        this.element = element;\n        this.pushService = Application.services.pushService;\n        this.pushRegistrations = [];\n\t\tthis.pushLinked = false;\n    };\n\n\tsetup() {\n\t\tvar context = this.element.props.context != null ? this.element.props.context() : \"\";\n\t\tvar key = this.element.props.id + context;\n\t\tif (Notifier.subscriptions[key]) return;\n\t\tNotifier.subscriptions[key] = true;\n\t\tthis.when(\"addInstance\").toSelf().execute((parameters) => this.element.addInstance(parameters));\n\t\tthis.when(\"addInstances\").toSelf().execute((parameters) => this.element.addInstances(parameters));\n\t\tthis.when(\"insertInstance\").toSelf().execute((parameters) => this.element.insertInstance(parameters));\n\t\tthis.when(\"insertInstances\").toSelf().execute((parameters) => this.element.insertInstances(parameters));\n\t\tthis.when(\"removeInstance\").toSelf().execute((parameters) => this.element.removeInstance(parameters));\n\t\tthis.when(\"clearContainer\").toSelf().execute((parameters) => this.element.clearContainer(parameters));\n\t};\n\n\twhen = (message) => {\n\t\tvar register = _register.bind(this);\n\t\tvar element = this.element;\n\t\tvar pushService = this.pushService;\n\t\treturn {\n\t\t\ttoSelf: function () {\n\t\t\t\treturn {\n\t\t\t\t\texecute: function (action) {\n\t\t\t\t\t\tregister(pushService.listen(message, function (parameters) {\n\t\t\t\t\t\t\tlet id = parameters.i;\n\t\t\t\t\t\t\tlet ownerPath = parameters.o;\n\t\t\t\t\t\t\tlet context = element.props.context != null ? element.props.context() : null;\n\t\t\t\t\t\t\tif (id === element.props.id && (ownerPath == null || ownerPath === \"\" || ownerPath === context || _containsAll(ownerPath, context))) {\n\t\t\t\t\t\t\t\taction(parameters);\n\t\t\t\t\t\t\t}\n\t\t\t\t\t\t}));\n\t\t\t\t\t}\n\t\t\t\t}\n\t\t\t},\n\t\t\texecute: function (action) {\n\t\t\t\tregister(pushService.listen(message, function(parameters) {\n\t\t\t\t\tif (parameters.n === element.name) {\n\t\t\t\t\t\taction(parameters);\n\t\t\t\t\t}\n\t\t\t\t}));\n\t\t\t}\n\t\t};\n\n\t\tfunction _containsAll(owner, context) {\n\t\t\tif (context == null) return false;\n\t\t\tlet contextList = context.split(\".\");\n\t\t\tfor (var i=0; i<contextList.length; i++) {\n\t\t\t\tif (owner.indexOf(contextList[i]) === -1) return false;\n\t\t\t}\n\t\t\treturn true;\n\t\t}\n\n\t\tfunction _register(registration) {\n\t\t\tthis.pushRegistrations.push(registration);\n\t\t}\n\t};\n\n\tdetached = () => {\n        var context = this.element.props.context != null ? this.element.props.context() : \"\";\n        var key = this.element.props.id + context;\n        Notifier.subscriptions[key] = null;\n\t\tthis.pushRegistrations.forEach((registrations) => registrations.deregister());\n\t\tthis.pushRegistrations.splice(0, this.pushRegistrations.length);\n\t};\n}")),
			rule().condition((type("requester"))).output(literal("export default class Requester {\n\tconstructor(element) {\n\t\tthis.element = element;\n\t\tthis.pushService = Application.services.pushService;\n\t\tthis.fileService = Application.services.fileService;\n\t};\n}"))
		);
	}
}