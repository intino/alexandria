package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class WebPackTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("webpack"))).output(literal("const path = require(\"path\");\nconst HtmlWebPackPlugin = require(\"html-webpack-plugin\");\nconst CopyWebpackPlugin = require('copy-webpack-plugin');\nconst CircularDependencyPlugin = require('circular-dependency-plugin');\n\nmodule.exports = {\n    module: {\n        rules: [\n            {\n                test: /\\.js$/,\n                ")).output(mark("exclude")).output(literal("\n                options: {\n                    rootMode: \"upward\",\n                    presets: ['@babel/preset-env'],\n                    cacheDirectory: true\n                },\n                loader: \"babel-loader\"\n            },\n            {\n                test: /\\.html$/,\n                loader: \"html-loader\"\n            },\n            {\n                test: /\\.css$/,\n                loader: 'style-loader!css-loader'\n            }\n        ]\n    },\n    entry : {\n        ")).output(mark("page", "gen").multiple(",\n")).output(literal("\n    },\n    output: {\n        path: \"")).output(mark("outDirectory")).output(literal("/")).output(mark("webModuleName")).output(literal("/www/")).output(mark("webModuleName")).output(literal("\",\n        publicPath: '$basePath/")).output(mark("webModuleName")).output(literal("/',\n        filename: \"[name].js\"\n    },\n    resolve: {\n        alias: {\n            ")).output(expression().output(mark("alias"))).output(literal("\n            \"app-elements\": path.resolve(__dirname, '.'),\n            \"")).output(mark("webModuleName")).output(literal("\": path.resolve(__dirname, '.')\n        }\n    },\n    plugins: [\n        new CircularDependencyPlugin({\n            failOnError: false,\n            allowAsyncCycles: false,\n            cwd: process.cwd(),\n        }),\n        new CopyWebpackPlugin([{\n            from: 'res',\n            to: './res'\n        }]),\n        ")).output(mark("page", "plugin").multiple(",\n")).output(literal("\n    ]\n};")),
			rule().condition((allTypes("exclude","alexandriaProject"))).output(literal("exclude: /(node_modules)/,")),
			rule().condition((type("exclude"))).output(literal("exclude: /node_modules\\/(?!(")).output(mark("use").multiple("|")).output(literal(")\\/).*/,")),
			rule().condition((allTypes("alias","alexandriaProject"))).output(literal("\"alexandria-ui-elements\": path.resolve(__dirname, '.'),")),
			rule().condition((type("page")), (trigger("gen"))).output(literal("'")).output(mark("templateName")).output(literal("' : './gen/apps/")).output(mark("templateName", "firstUppercase")).output(literal(".js'")),
			rule().condition((type("page")), (trigger("plugin"))).output(literal("new HtmlWebPackPlugin({\n    hash: true,\n    title: \"Test UI\",\n    chunks: ['")).output(mark("templateName")).output(literal("'],\n    template: \"./src/")).output(mark("templateName")).output(literal(".html\",\n    filename: \"./")).output(mark("templateName")).output(literal(".html\"\n})"))
		);
	}
}