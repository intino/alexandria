import React from "react";
import ReactDOM from "react-dom";
import AlexandriaDisplay from "../Display";

export default class Component extends AlexandriaDisplay {

    constructor(props) {
        super(props);
    };

    style() {
        const { styleName, theme } = this.props;
        return styleName != null ? theme.componentStyles[styleName] : undefined;
    };
}