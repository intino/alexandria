package io.intino.konos.builder.codegeneration.schema;

import io.intino.itrules.Frame;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.Schema;

import java.io.File;

public abstract class SchemaWriter {
	protected final CompilationContext context;
	protected final File destination;
	protected final String packageName;

	public abstract void write(Schema schema, Frame frame);

	public SchemaWriter(CompilationContext context, File destination, String packageName) {
		this.context = context;
		this.destination = destination;
		this.packageName = packageName;
	}

	public File destination() {
		return destination;
	}

	public String packageName() {
		return packageName;
	}

	protected File schemaFolder(Schema schema) {
		return new File(destination, SchemaHelper.subPackage(schema));
	}

}
