package io.intino.konos.builder.codegeneration.schema;

import io.intino.itrules.Engine;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.template.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Schema;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.javaFile;

public class DefaultSchemaWriter extends SchemaWriter {
	private final boolean serializationAnnotations;

	public DefaultSchemaWriter(CompilationContext context, File destination, String packageName, boolean serializationAnnotations) {
		super(context, destination, packageName);
		this.serializationAnnotations = serializationAnnotations;
	}

	@Override
	public void write(Schema schema, Frame frame) {
		File packageFolder = schemaFolder(schema);
		Commons.writeFrame(packageFolder, schema.name$(), new Engine(template()).addAll(Formatters.all).render(new FrameBuilder("root").add("root", packageName).add("package", packageName(schema)).add("schema", frame)));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(schema), javaFile(packageFolder, schema.name$()).getAbsolutePath()));
	}

	private String packageName(Schema schema) {
		String subPackage = SchemaHelper.subPackage(schema);
		return subPackage.isEmpty() ? packageName : packageName + "." + subPackage.replace(File.separator, ".");
	}

	private Template template() {
		return serializationAnnotations ? new SchemaAnnotatedTemplate() : new SchemaTemplate();
	}

}
