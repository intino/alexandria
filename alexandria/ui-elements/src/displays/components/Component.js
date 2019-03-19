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
        const theme = Theme.get();
        return format != null ? theme.formats[format] : undefined;
    };
}