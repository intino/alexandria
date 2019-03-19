import React from "react";
import ReactDOM from "react-dom";
import AlexandriaDisplay from "../Display";
import Theme from "../../../gen/Theme";

export default class Component extends AlexandriaDisplay {

    constructor(props) {
        super(props);
    };

    style() {
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
}