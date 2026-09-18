import AbstractBaseDate from "../../../gen/displays/components/AbstractBaseDate";
import {normalizeHighlight, withHighlight, withHighlightBackground} from "./Highlight";

export default class BaseDate extends AbstractBaseDate {

	constructor(props) {
		super(props);
		this.state = {...this.state, highlighted: normalizeHighlight(this.props.highlighted)};
	};

	refreshHighlight = (highlighted) => {
		this.setState({ highlighted: normalizeHighlight(highlighted, this.props.highlighted) });
	};

	style() {
		return super.style();
	};

	highlightStyle() {
		return withHighlight({}, this.state.highlighted);
	};

	highlightBackgroundStyle() {
		return withHighlightBackground({}, this.state.highlighted);
	};

	refresh = (value) => {
	};
}
