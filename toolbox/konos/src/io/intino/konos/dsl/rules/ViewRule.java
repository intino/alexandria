package io.intino.konos.dsl.rules;

import io.intino.tara.language.model.Mogram;
import io.intino.tara.language.model.rules.MogramRule;

import java.util.List;

import static io.intino.konos.dsl.rules.ViewRule.ERROR_TYPE.*;

public class ViewRule implements MogramRule {

	enum ERROR_TYPE {
		NAME, SIZE, COMPONENT_NOT_ALLOWED
	}

	private ERROR_TYPE error = NAME;

	public boolean accept(Mogram mogram) {
		final List<Mogram> components = mogram.components();
		if (components.size() != 1) {
			error(SIZE);
			return false;
		}
		return components.stream().noneMatch(component -> hasForbiddenTypes(component) || (mogram.isAnonymous() && (hasFilteredCatalog(component) || isRenderDisplay(component))));
	}

	private boolean hasForbiddenTypes(Mogram component) {
		return component.types().contains("RenderPanels") ||
				component.types().contains("RenderObjects") && error(COMPONENT_NOT_ALLOWED);
	}

	private boolean hasFilteredCatalog(Mogram component) {
		return component.types().contains("RenderCatalogs") &&
				component.parameters().stream().anyMatch(p -> p.name().equals("filtered") &&
						p.values().get(0).toString().equals("true")) && error(NAME);
	}

	private boolean isRenderDisplay(Mogram component) {
		return component.types().contains("RenderDisplay") && error(NAME);
	}

	@Override
	public String errorMessage() {
		return switch (error) {
			case NAME -> "This View must have name";
			case SIZE -> "This View must have just one Render";
			case COMPONENT_NOT_ALLOWED -> "RenderPanels and RenderObjects are not allowed here";
		};
	}

	private boolean error(ERROR_TYPE type) {
		error = type;
		return true;
	}
}