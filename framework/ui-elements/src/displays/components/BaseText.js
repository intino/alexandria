import React from "react";
import AbstractBaseText from "../../../gen/displays/components/AbstractBaseText";

export default class BaseText extends AbstractBaseText {

    constructor(props) {
        super(props);
        this.state = {
            ...this.state,
            highlighted : this.props.highlighted,
        }
    };

    refresh = (value) => {
    };

    refreshHighlight = (highlighted) => {
        this.setState({ highlighted: { text: this._textColor(highlighted), background: this._backgroundColor(highlighted) }});
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