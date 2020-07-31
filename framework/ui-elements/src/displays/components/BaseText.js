import React from "react";
import AbstractBaseText from "../../../gen/displays/components/AbstractBaseText";

export default class BaseText extends AbstractBaseText {

    constructor(props) {
        super(props);
        this.state = {
            ...this.state,
			error: null,
			value : this.props.value,
			title : this.props.value,
            highlighted : this.props.highlighted,
        }
    };

    refresh = (value) => {
        const finalValue = this._requireEllipsis(value) ? value.substring(0, this.props.cropWithEllipsis) + "..." : value;
		this.setState({ value: finalValue != null ? finalValue : "", title: value != null ? value : "", error: null });
    };

    refreshError = (value) => {
		this.setState({ error: value });
    };

    refreshHighlight = (highlighted) => {
        this.setState({ highlighted: { text: this._textColor(highlighted), background: this._backgroundColor(highlighted) }});
    };

    _requireEllipsis = (value) => {
        return this.props.cropWithEllipsis != null && value != null && value.length > this.props.cropWithEllipsis;
    };

    _textColor = (highlighted) => {
        if (highlighted != null && highlighted.textColor != null) return highlighted.textColor;
        return this.props.highlighted != null ? this.props.highlighted.text : null;
    };

    _backgroundColor = (highlighted) => {
        if (highlighted != null && highlighted.backgroundColor != null) return highlighted.backgroundColor;
        return this.props.highlighted != null ? this.props.highlighted.background : null;
    };

}