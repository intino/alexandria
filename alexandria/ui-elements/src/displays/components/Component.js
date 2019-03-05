import React from "react";
import ReactDOM from "react-dom";
import AlexandriaDisplay from "../Display";
import Theme from "../../../gen/Theme";

export default class Component extends AlexandriaDisplay {

    constructor(props) {
        super(props);
    };

    style() {
        const { styleName } = this.props;
        const theme = Theme.get();
        return styleName != null ? theme.componentStyles[styleName] : undefined;
    };
}