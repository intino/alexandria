import React from "react";
import ReactDOM from "react-dom";
import AlexandriaDisplay from "../Display";
import Theme from "../../../gen/Theme";

export default class Component extends AlexandriaDisplay {

    constructor(props) {
        super(props);
    };

    style() {
        let style = this._addFormats();
        style = this._addSpacing(style);
        return style;
    };

    _addFormats() {
        const { format } = this.props;
        const formats = format != null ? format.split(" ") : [];
        if (formats.length <= 0) return undefined;
        const theme = Theme.get();
        const result = {};
        formats.forEach(f => {
            let style = theme.formats[f];
            if (style == null) return;
            for (let rule in style) result[rule] = style[rule];
        });
        return result;
    };

    _addSpacing(style) {
        if (this.props.spacingStyle == null) return style;
        if (style == null) style = {};
        let spacingStyle = this.props.spacingStyle;
        if (spacingStyle.indexOf("bottom:") !== -1) style.marginBottom = spacingStyle.replace("bottom:", "") + "px";
        else style.marginRight = spacingStyle.replace("right:", "") + "px";
        return style;
    }

    applyStyles = (styles, to) => {
        if (styles == null) return;
        for (let rule in styles) to[rule] = styles[rule];
    };

    _widthDefined = () => {
        return this.props.width != null && this.props.width.indexOf("-1") === -1;
    };

    _heightDefined = () => {
        return this.props.height != null && this.props.height.indexOf("-1") === -1;
    };
}