package io.intino.konos.builder.codegeneration.swagger;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class IndexTemplate extends Template {

	protected IndexTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new IndexTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "index"))).add(literal("<!DOCTYPE html>\n<head>\n  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n\n  <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/bootstrap.css\" media=\"screen\">\n  <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/bootstrap-responsive.css\" media=\"screen\">\n  <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/style.css\">\n  <script src=\"assets/js/jquery-1.8.3.min.js\" type=\"text/javascript\"></script>\n  <script src=\"assets/js/bootstrap.js\"></script>\n  <script src=\"assets/js/parameters.js\" type=\"text/javascript\"></script>\n\n  <script>\n    const Dictionary = {\n      es: {\n        \"ServiceList\" : \"lista de servicios de ::application::\"\n      },\n      en: {\n        \"ServiceList\" : \"service list for ::application::\"\n      }\n    }\n\n    const Templates = {\n      Service: \"<li><div><a href='::name::/docs/index.html'>::label::</a></div><div class='description'>::description::</div></li>\"\n    };\n\n    const configuration = {\n      title: \"API docs\",\n      services : [\n        ")).add(mark("service").multiple(",\n")).add(literal("\n      ]\n    };\n\n    function render() {\n      loadDictionary(configuration);\n      renderApplication(configuration);\n    }\n\n    function loadDictionary(configuration) {\n      Dictionary.es.ServiceList = Dictionary.es.ServiceList.replace(\"::application::\", configuration.application);\n      Dictionary.en.ServiceList = Dictionary.en.ServiceList.replace(\"::application::\", configuration.application);\n    }\n\n    function renderApplication(configuration) {\n\t  document.getElementById(\"header\").style.display = findGetParameter(\"embedded\") != null ? \"none\" : \"block\";\n\t  document.getElementById(\"title\").innerHTML = configuration.title;\n\n\t  renderTabs(configuration);\n\t  renderTabsContent(configuration);\n\t}\n\n\tfunction renderTabs(configuration) {\n\t\tvar tabs = \"\";\n\n\t\tfor (var i=0; i<configuration.services.length; i++) {\n\t\t\tvar service = configuration.services[i];\n\t\t\tvar clazz = (i == 0) ? ' class=\"active\"' : '';\n\t\t\ttabs += '<li' + clazz + '><a data-toggle=\"tab\" href=\"#' + service.name + '\">' + service.label + '</a></li>';\n\t\t}\n\n\t\tdocument.getElementById(\"tabs\").innerHTML = tabs;\n\t}\n\n\tfunction renderTabsContent(configuration) {\n\t\tvar tabsContent = \"\";\n\n\t\tfor (var i=0; i<configuration.services.length; i++) {\n\t\t\tvar service = configuration.services[i];\n\t\t\tvar clazz = (i == 0) ? ' in active' : '';\n\t\t\tvar source = service.name + '/docs/index.html';\n\n\t\t\ttabsContent += '<div id=\"' + service.name + '\" class=\"tab-pane fade' + clazz + '\">';\n\t\t\ttabsContent += '<iframe id=\"frame_' + service.name + '\" src=\"' + source + '\" onload=\"removeHeadersIfEmbedded(\\'frame_' + service.name + '\\')\"></iframe>';\n\t\t\ttabsContent += '</div>';\n\t\t}\n\n\t\tdocument.getElementById(\"tabsContent\").innerHTML = tabsContent;\n\t}\n\n\tfunction removeHeadersIfEmbedded(frameId) {\n\t\tvar frame = document.getElementById(frameId);\n\t\tframe.contentDocument.querySelectorAll(\".header\").forEach(function(header) { header.style.display = \"none\"; });\n\t\tframe.contentDocument.querySelectorAll(\".content\").forEach(function(content) { content.style.paddingTop = \"0\"; });\n\t}\n  </script>\n\n  <style>\n\thtml, body {\n\t  height: 100%;\n\t}\n\t#content {\n\t  padding: 0;\n\t  margin: 0;\n\t  height: 100%;\n\t}\n\t.nav-tabs {\n\t  padding-top: 10px;\n\t  margin-bottom: 5px;\n\t}\n\t.tab-content {\n\t  height: calc(100% - 53px);\n\t}\n\t.tab-content > div {\n\t  height: 100%;\n\t}\n\t.tab-content > div iframe {\n\t  border: 0;\n\t  width: 100%;\n\t  height: 100%;\n\t}\n  </style>\n\n  <title>")).add(mark("application")).add(literal("</title>\n</head>\n<body onload=\"render()\">\n  <div id=\"header\" class=\"header\">\n    <div class=\"top-bar\">\n      <h1 id=\"logo\"><a class=\"logo\" href=\"/\"><span><img src=\"assets/logo.png\"></img></span><span id=\"title\" class=\"light\"></span></a></h1>\n    </div>\n  </div>\n  <div class=\"model-container\"></div>\n  <div id=\"content\" class=\"content\">\n\t<ul id=\"tabs\" class=\"nav nav-tabs\"></ul>\n\t<div id=\"tabsContent\" class=\"tab-content\"></div>\n  </div>\n</body>\n</html>")),
			rule().add((condition("type", "service")), (condition("trigger", "service"))).add(literal("{ name: \"")).add(mark("name")).add(literal("\", label: \"")).add(mark("name")).add(literal("\", description: \"")).add(mark("description")).add(literal("\" }"))
		);
		return this;
	}
}