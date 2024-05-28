package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.literal;

public class PassiveViewTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("notifier")).output(literal("export default class Notifier {\n\tstatic subscriptions = [];\n\n\tconstructor(element) {\n\t\tthis.element = element;\n\t\tthis.pushService = Application.services.pushService;\n\t\tthis.pushRegistrations = [];\n\t\tthis.pushLinked = false;\n\t};\n\n\tsetup() {\n\t\tvar context = this.element.props.context != null ? this.element.props.context() : \"\";\n\t\tvar key = this.element.props.id + context;\n\t\t//if (Notifier.subscriptions[key]) return;\n\t\t//Notifier.subscriptions[key] = true;\n\t\tthis.when(\"addInstance\").toSelf().execute((parameters) => this.element.addInstance(parameters));\n\t\tthis.when(\"addInstances\").toSelf().execute((parameters) => this.element.addInstances(parameters));\n\t\tthis.when(\"insertInstance\").toSelf().execute((parameters) => this.element.insertInstance(parameters));\n\t\tthis.when(\"insertInstances\").toSelf().execute((parameters) => this.element.insertInstances(parameters));\n\t\tthis.when(\"removeInstance\").toSelf().execute((parameters) => this.element.removeInstance(parameters));\n\t\tthis.when(\"clearContainer\").toSelf().execute((parameters) => this.element.clearContainer(parameters));\n\t\tthis.when(\"redirect\").toSelf().execute((parameters) => this.element.redirect(parameters));\n\t\tthis.when(\"addressed\").toSelf().execute((parameters) => this.element.addressed(parameters));\n\t\tthis.when(\"closeClient\").toSelf().execute((parameters) => this.element.closeClient(parameters));\n\t};\n\n\twhen = (message) => {\n\t\tvar register = _register.bind(this);\n\t\tvar element = this.element;\n\t\tvar pushService = this.pushService;\n\t\treturn {\n\t\t\ttoSelf: function () {\n\t\t\t\treturn {\n\t\t\t\t\texecute: function (action) {\n\t\t\t\t\t\tregister(pushService.listen(message, function (parameters) {\n\t\t\t\t\t\t\tlet id = parameters.i;\n\t\t\t\t\t\t\tlet ownerPath = parameters.o;\n\t\t\t\t\t\t\tlet context = element.props.context != null ? element.props.context() : null;\n\t\t\t\t\t\t\tlet elementId = element.shortId();\n\t\t\t\t\t\t\tif (id === elementId && (ownerPath == null || ownerPath === \"\" || ownerPath === context || _containsAll(ownerPath, context))) {\n\t\t\t\t\t\t\t\taction(parameters);\n\t\t\t\t\t\t\t}\n\t\t\t\t\t\t}));\n\t\t\t\t\t}\n\t\t\t\t}\n\t\t\t},\n\t\t\texecute: function (action) {\n\t\t\t\tregister(pushService.listen(message, function(parameters) {\n\t\t\t\t\tif (parameters.n === element.name) {\n\t\t\t\t\t\taction(parameters);\n\t\t\t\t\t}\n\t\t\t\t}));\n\t\t\t}\n\t\t};\n\n\t\tfunction _containsAll(owner, context) {\n\t\t\tif (context == null) return false;\n\t\t\tlet contextList = context.split(\".\");\n\t\t\tlet ownerList = owner.split(\".\");\n\t\t\treturn ownerList.length > contextList.length ? _containsInList(owner, contextList) : _containsInList(context, ownerList);\n\t\t}\n\n\t\tfunction _containsInList(content, list) {\n\t\t\tfor (var i=0; i<list.length; i++) {\n\t\t\t\tif (content.indexOf(list[i]) === -1) return false;\n\t\t\t}\n\t\t\treturn true;\n\t\t}\n\n\t\tfunction _register(registration) {\n\t\t\tthis.pushRegistrations.push(registration);\n\t\t}\n\t};\n\n\tdetached = () => {\n\t\tvar context = this.element.props.context != null ? this.element.props.context() : \"\";\n\t\tvar key = this.element.props.id + context;\n\t\t//Notifier.subscriptions[key] = null;\n\t\tthis.pushRegistrations.forEach((registrations) => registrations.deregister());\n\t\tthis.pushRegistrations.splice(0, this.pushRegistrations.length);\n\t};\n}")));
		rules.add(rule().condition(allTypes("requester")).output(literal("import history from \"alexandria-ui-elements/src/util/History\";\n\nexport default class Requester {\n\tconstructor(element) {\n\t\tthis.element = element;\n\t\tthis.pushService = Application.services.pushService;\n\t\tthis.fileService = Application.services.fileService;\n\t};\n\n\tavailable = (unit) => {\n\t\treturn this.pushService.isConnectionRegistered(unit);\n\t};\n\n\tconnect = (unit, successCallback, errorCallback) => {\n\t\tif (this.available(unit)) {\n\t\t\tif (successCallback != null) successCallback();\n\t\t\treturn;\n\t\t}\n\t\tconst pushConnections = Application.configuration.pushConnections;\n\t\tfor (let i=0; i<pushConnections.length; i++) {\n\t\t\tconst connection = pushConnections[i].split(\"_##_\");\n\t\t\tif (connection[0].toLowerCase() !== unit.toLowerCase()) continue;\n\t\t\tthis.pushService.openConnection(connection[0], connection[1], successCallback, errorCallback);\n\t\t}\n\t};\n\n\taddToHistory = (param) => {\n\t\tconst address = this.element.historyAddress();\n\t\tconst id = (param instanceof Array) ? (param.length > 0 ? param[0] : \"\") : param;\n\t\tif (address == null) return false;\n\t\thistory.push(address.replace(/:[^\\/]*/, id), {});\n\t\treturn true;\n\t};\n}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}