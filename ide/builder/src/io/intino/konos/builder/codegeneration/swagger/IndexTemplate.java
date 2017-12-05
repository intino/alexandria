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
			rule().add((condition("type", "index"))).add(literal("<!DOCTYPE html>\n<head>\n  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n\n  <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/bootstrap.css\" media=\"screen\">\n  <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/bootstrap-responsive.css\" media=\"screen\">\n  <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/style.css\">\n  <script src=\"assets/js/jquery-1.8.3.min.js\" type=\"text/javascript\"></script>\n  <script src=\"assets/js/parameters.js\" type=\"text/javascript\"></script>\n\n  <script>\n    const Dictionary = {\n      es: {\n        \"ServiceList\" : \"lista de servicios de ::application::\"\n      },\n      en: {\n        \"ServiceList\" : \"service list for ::application::\"\n      }\n    }\n\n    const Templates = {\n      Service: \"<li><div><a href='::name::/docs/index.html'>::label::</a></div><div class='description'>::description::</div></li>\"\n    };\n\n    const configuration = {\n      title: \"API docs\",\n      services : [\n        ")).add(mark("service").multiple(",\n")).add(literal("\n      ]\n    };\n\n    function render() {\n      loadDictionary(configuration);\n      renderApplication(configuration);\n    }\n\n    function loadDictionary(configuration) {\n      Dictionary.es.ServiceList = Dictionary.es.ServiceList.replace(\"::application::\", configuration.application);\n      Dictionary.en.ServiceList = Dictionary.en.ServiceList.replace(\"::application::\", configuration.application);\n    }\n\n    function renderApplication(configuration) {\n      var services = \"\";\n\n\t  document.getElementById(\"header\").style.display = findGetParameter(\"embedded\") != null ? \"none\" : \"block\";\n\n\t  if (configuration.services.length == 1)\n\t\t  window.location.href = configuration.services[0].name + \"/docs/index.html\";\n\n      for (var i=0; i<configuration.services.length; i++) {\n        var service = configuration.services[i];\n        var content = Templates.Service;\n        content = content.replace(\"::name::\", service.name);\n        content = content.replace(\"::label::\", service.label);\n        content = content.replace(\"::description::\", service.description);\n        services += content;\n      }\n\n      document.getElementById(\"content\").innerHTML = \"<ul class='summary'>\" + services + \"</ul>\";\n      document.getElementById(\"title\").innerHTML = configuration.title;\n    }\n  </script>\n\n  <title>")).add(mark("application")).add(literal("</title>\n</head>\n<body onload=\"render()\">\n  <div id=\"header\" class=\"header\">\n    <div class=\"top-bar\">\n      <h1 id=\"logo\"><a class=\"logo\" href=\"/\"><span><img src=\"assets/logo.png\"></img></span><span id=\"title\" class=\"light\"></span></a></h1>\n    </div>\n  </div>\n  <div class=\"model-container\"></div>\n  <div id=\"content\" class=\"content\">loading...</div>\n</body>\n</html>")),
			rule().add((condition("type", "service")), (condition("trigger", "service"))).add(literal("{ name: \"")).add(mark("name")).add(literal("\", label: \"")).add(mark("name")).add(literal("\", description: \"\" },"))
		);
		return this;
	}
}