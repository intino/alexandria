import AbstractBaseFile from "../../../gen/displays/components/AbstractBaseFile";
import {normalizeHighlight, withHighlight, withHighlightBackground} from "./Highlight";

export default class BaseFile extends AbstractBaseFile {

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
