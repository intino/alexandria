package io.intino.pandora.plugin.codegeneration.exception;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

public class ExceptionRenderer {

    private static final String EXCEPTIONS = "exceptions";
	private final List<io.intino.pandora.plugin.Exception> exceptions;
	private File gen;
    private String packageName;

    public ExceptionRenderer(Graph graph, File gen, String packageName) {
		exceptions = graph.find(io.intino.pandora.plugin.Exception.class);
		this.gen = gen;
        this.packageName = packageName;
    }

    public void execute() {
        exceptions.forEach(this::processException);
    }

	private void processException(io.intino.pandora.plugin.Exception exception) {
//        if(exception.name())
        Frame frame = new Frame().addTypes("exception");
        frame.addSlot("name", exception.name());
        frame.addSlot("package", packageName);
    }

    private Template template() {
        return ExceptionTemplate.create();
    }

}
