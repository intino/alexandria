import AbstractBaseText from "../../../gen/displays/components/AbstractBaseText";
import {normalizeHighlight, withHighlight, withHighlightBackground} from "./Highlight";

export default class BaseText extends AbstractBaseText {

    constructor(props) {
        super(props);
        this.state = {
            ...this.state,
			error: null,
			value : this.props.value,
			title : this.props.value,
			highlighted : normalizeHighlight(this.props.highlighted),
        }
    };

    refresh = (value) => {
        const finalValue = this._requireEllipsis(value) ? value.substring(0, this.props.cropWithEllipsis) + "..." : value;
		this.setState({ value: finalValue != null ? finalValue : "", title: value != null ? value : "" });
    };

    refreshError = (value) => {
		this.setState({ error: value });
    };

    refreshHighlight = (highlighted) => {
        this.setState({ highlighted: normalizeHighlight(highlighted, this.props.highlighted) });
    };

    _requireEllipsis = (value) => {
        return this.props.cropWithEllipsis != null && value != null && value.length > this.props.cropWithEllipsis;
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

}
