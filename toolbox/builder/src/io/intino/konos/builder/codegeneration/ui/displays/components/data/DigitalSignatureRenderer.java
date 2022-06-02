package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.DataComponents.DigitalSignature;

public class DigitalSignatureRenderer extends ComponentRenderer<DigitalSignature> {

	public DigitalSignatureRenderer(CompilationContext compilationContext, DigitalSignature component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.text() != null) result.add("text", element.text());
		addAfirmaFacet(result);
		return result;
	}

	private void addAfirmaFacet(FrameBuilder result) {
		if (!element.isAutoFirma()) return;
		result.add("autofirma");
		DigitalSignature.AutoFirma autoFirma = element.asAutoFirma();
		DigitalSignature.AutoFirma.SignFormat format = autoFirma.signFormat();
		if (format != null) result.add("signFormat", format.name());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("digitalsignature", "");
	}
}
