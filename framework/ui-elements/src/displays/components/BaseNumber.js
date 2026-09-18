import AbstractBaseNumber from "../../../gen/displays/components/AbstractBaseNumber";
import {normalizeHighlight, withHighlight, withHighlightBackground} from "./Highlight";

export default class BaseNumber extends AbstractBaseNumber {

	constructor(props) {
		super(props);
		this.state = {
			error: null,
			value : this.props.value,
		    decimals: this.props.decimals != null ? this.props.decimals : 0,
			expanded : this.props.expanded,
			style: this.props.style,
			prefix : this.props.prefix,
			suffix : this.props.suffix,
			...this.state,
			highlighted: normalizeHighlight(this.props.highlighted),
		}
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

    refreshError = (value) => {
		this.setState({ error: value });
    };

	refreshExpanded = (value) => {
		this.setState({expanded : value});
	};

	refreshDecimals = (value) => {
		this.setState({decimals : value});
	};

	refreshPrefix = (value) => {
		this.setState({prefix : value});
	};

	refreshSuffix = (value) => {
		this.setState({suffix : value});
	};

	refreshStyle = (value) => {
		this.setState({style : value});
	};
}
