package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.itrules.Engine;
import io.intino.itrules.Formatter;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.accessor.ui.android.templates.SchemaTemplate;
import io.intino.konos.builder.codegeneration.schema.SchemaHelper;
import io.intino.konos.builder.codegeneration.schema.SchemaWriter;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Schema;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Logger;

import static io.intino.konos.builder.helpers.Commons.kotlinFile;

public class AndroidSchemaWriter extends SchemaWriter {
	private static final java.util.logging.Logger LOG = Logger.getGlobal();

	public AndroidSchemaWriter(CompilationContext context) {
		super(context, context.gen(Target.MobileShared), context.packageName() + ".mobile.schemas");
	}

	@Override
	public void write(Schema schema, Frame frame) {
		try {
			File packageFolder = schemaFolder(schema);
			String content = new Engine(new SchemaTemplate()).add("typeFormat", typeFormatter()).render(new FrameBuilder("root").add("root", packageName).add("package", packageName).add("schema", frame));
			packageFolder.mkdirs();
			File file = kotlinFile(packageFolder, schema.name$());
			Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(schema), kotlinFile(packageFolder, schema.name$()).getAbsolutePath()));
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}
	}

	private Formatter typeFormatter() {
		return (value) -> {
			if (value.toString().contains(".")) return Formatters.firstLowerCase(value.toString());
			else return value;
		};
	}

	@Override
	protected File schemaFolder(Schema schema) {
		return new File(destination, SchemaHelper.subPackage(schema));
	}

}
