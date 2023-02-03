package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class WebPackTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("webpack"))).output(literal("const path = require(\"path\");\nconst HtmlWebPackPlugin = require(\"html-webpack-plugin\");\nconst CopyWebpackPlugin = require('copy-webpack-plugin');\nconst CircularDependencyPlugin = require('circular-dependency-plugin');\n\nmodule.exports = {\n\tmodule: {\n\t\trules: [\n\t\t\t{\n\t\t\t\ttest: /\\.js$/,\n\t\t\t\t")).output(mark("exclude")).output(literal("\n\t\t\t\toptions: {\n\t\t\t\t\trootMode: \"upward\",\n\t\t\t\t\tpresets: ['@babel/preset-env'],\n\t\t\t\t\tcacheDirectory: true\n\t\t\t\t},\n\t\t\t\tloader: \"babel-loader\"\n\t\t\t},\n\t\t\t{\n\t\t\t\ttest: /\\.html$/,\n\t\t\t\tloader: \"html-loader\"\n\t\t\t},\n\t\t\t{\n\t\t\t\ttest: /\\.css$/,\n\t\t\t\tloader: 'style-loader!css-loader'\n\t\t\t}\n\t\t]\n\t},\n\tentry : {\n\t\t")).output(mark("page", "gen").multiple(",\n")).output(literal("\n\t},\n\toutput: {\n\t\tpath: \"")).output(mark("outDirectory")).output(literal("/")).output(mark("serviceName")).output(literal("/www/")).output(mark("serviceName")).output(literal("\",\n\t\tpublicPath: '$basePath/")).output(mark("serviceName")).output(literal("/',\n\t\tfilename: \"[name].js\"\n\t},\n\tresolve: {\n\t\talias: {\n\t\t\t")).output(expression().output(mark("alias"))).output(literal("\n\t\t\t\"app-elements\": path.resolve(__dirname, '.'),\n\t\t\t\"")).output(mark("serviceName")).output(literal("\": path.resolve(__dirname, '.')\n\t\t}\n\t},\n\tplugins: [\n\t\tnew CircularDependencyPlugin({\n\t\t\tfailOnError: false,\n\t\t\tallowAsyncCycles: false,\n\t\t\tcwd: process.cwd(),\n\t\t}),\n\t\tnew CopyWebpackPlugin([{\n\t\t\tfrom: 'res',\n\t\t\tto: './res'\n\t\t}]),\n\t\t")).output(mark("page", "plugin").multiple(",\n")).output(literal("\n\t]\n};")),
			rule().condition((allTypes("exclude","alexandriaProject"))).output(literal("exclude: /(node_modules)/,")),
			rule().condition((type("exclude"))).output(literal("exclude: /node_modules\\/(?!(")).output(mark("use").multiple("|")).output(literal(")\\/).*/,")),
			rule().condition((allTypes("alias","alexandriaProject"))).output(literal("\"alexandria-ui-elements\": path.resolve(__dirname, '.'),")),
			rule().condition((type("page")), (trigger("gen"))).output(literal("'")).output(mark("templateName")).output(literal("' : './gen/apps/")).output(mark("templateName", "firstUppercase")).output(literal(".js'")),
			rule().condition((type("page")), (trigger("plugin"))).output(literal("new HtmlWebPackPlugin({\n\thash: true,\n\ttitle: \"Test UI\",\n\tchunks: ['")).output(mark("templateName")).output(literal("'],\n\ttemplate: \"./src/")).output(mark("templateName")).output(literal(".html\",\n\tfilename: \"./")).output(mark("templateName")).output(literal(".html\"\n})"))
		);
	}
}