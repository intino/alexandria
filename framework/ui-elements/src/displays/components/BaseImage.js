import React from "react";
import AbstractBaseImage from "../../../gen/displays/components/AbstractBaseImage";
import {normalizeHighlight, withHighlight, withHighlightBackground} from "./Highlight";

export default class BaseImage extends AbstractBaseImage {

	constructor(props) {
		super(props);
		this.reloaded = React.createRef();
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

	refresh = (info) => {
		this.setState({ value: info.value, darkValue: info.darkValue });
	};

}
