import React from "react";
import PassiveView from "./PassiveView";
import ReactDOM from "react-dom";

export default class Display extends PassiveView {

    constructor(props) {
        super(props);
    };

    add = (type, props, parent) =>  {
        const component = React.createElement(type, props);
        ReactDOM.render(component, parent);
    }
}