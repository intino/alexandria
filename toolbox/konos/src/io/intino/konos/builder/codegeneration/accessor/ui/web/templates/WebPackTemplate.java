package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class WebPackTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("webpack")).output(literal("const path = require(\"path\");\nconst HtmlWebPackPlugin = require(\"html-webpack-plugin\");\nconst CopyWebpackPlugin = require('copy-webpack-plugin');\nconst CircularDependencyPlugin = require('circular-dependency-plugin');\n\nmodule.exports = {\n\tmodule: {\n\t\trules: [\n            {\n                test: /node_modules\\/ace-linters*/,\n                use: { loader: 'babel-loader', options: { presets: ['@babel/preset-env'] } }\n            },\n            {\n                test: /\\.mjs$/,\n                include: /node_modules/,\n                type: \"javascript/auto\",\n                loader: \"babel-loader\"\n            },\n\t\t\t{\n\t\t\t\ttest: /\\.js$/,\n\t\t\t\t")).output(placeholder("exclude")).output(literal("\n\t\t\t\toptions: { rootMode: \"upward\", presets: ['@babel/preset-env'], cacheDirectory: true },\n\t\t\t\tloader: \"babel-loader\"\n\t\t\t},\n\t\t\t{\n\t\t\t\ttest: /\\.html$/,\n\t\t\t\tloader: \"html-loader\"\n\t\t\t},\n\t\t\t{\n\t\t\t\ttest: /\\.css$/,\n\t\t\t\tloader: 'style-loader!css-loader'\n\t\t\t}\n\t\t]\n\t},\n\tentry : {\n\t\t")).output(placeholder("page", "gen").multiple(",\n")).output(literal("\n\t},\n\toutput: {\n\t\tpath: \"")).output(placeholder("outDirectory")).output(literal("/")).output(placeholder("module")).output(literal("/www/")).output(placeholder("serviceName")).output(literal("\",\n\t\tpublicPath: '$basePath/")).output(placeholder("serviceName")).output(literal("/',\n\t\tfilename: \"[name].js\"\n\t},\n\tresolve: {\n\t\talias: {\n\t\t\t")).output(expression().output(placeholder("alias"))).output(literal("\n\t\t\t\"app-elements\": path.resolve(__dirname, '.'),\n\t\t\t\"")).output(placeholder("serviceName")).output(literal("\": path.resolve(__dirname, '.')\n\t\t}\n\t},\n\tplugins: [\n\t\tnew CircularDependencyPlugin({\n\t\t\tfailOnError: false,\n\t\t\tallowAsyncCycles: false,\n\t\t\tcwd: process.cwd(),\n\t\t}),\n\t\tnew CopyWebpackPlugin([{\n\t\t\tfrom: 'res',\n\t\t\tto: './res'\n\t\t}]),\n\t\t")).output(placeholder("page", "plugin").multiple(",\n")).output(literal("\n\t]\n};")));
		rules.add(rule().condition(allTypes("exclude", "alexandriaProject")).output(literal("exclude: /(node_modules)/,")));
		rules.add(rule().condition(allTypes("exclude")).output(literal("exclude: /node_modules\\/(?!(")).output(placeholder("use").multiple("|")).output(literal(")\\/).*/,")));
		rules.add(rule().condition(allTypes("alias", "alexandriaProject")).output(literal("\"alexandria-ui-elements\": path.resolve(__dirname, '.'),")));
		rules.add(rule().condition(all(allTypes("page"), trigger("gen"))).output(literal("'")).output(placeholder("templateName")).output(literal("' : './gen/apps/")).output(placeholder("templateName", "firstUppercase")).output(literal(".js'")));
		rules.add(rule().condition(all(allTypes("page"), trigger("plugin"))).output(literal("new HtmlWebPackPlugin({\n\thash: true,\n\ttitle: \"Test UI\",\n\tchunks: ['")).output(placeholder("templateName")).output(literal("'],\n\ttemplate: \"./src/")).output(placeholder("templateName")).output(literal(".html\",\n\tfilename: \"./")).output(placeholder("templateName")).output(literal(".html\"\n})")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}