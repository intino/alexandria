import React from "react";
import PassiveView from "./PassiveView";
import ReactDOM from "react-dom";
import * as Elements from "app-elements/gen/Displays";

export default class Display extends PassiveView {

    constructor(props) {
        super(props);
        this.translator = Application.services.translatorService;
    };

    add = (child) => {
        let elements = this.state.elements != null ? this.state.elements : [];
        elements.push(child);
        this.setState( { elements });
    };

    remove = (id) => {
        for (var i = 0; i < this.state.elements.length; i++)
            if (this.state.elements[i].i === id) break;
        if (i >= this.state.elements.length) return;
        this.setState({ elements: this.state.elements.splice(index, 1) });
    };

    translate = (word) => {
        return this.translator.translate(word);
    },

    renderElements = () => {
        if (this.state.elements == null) return;
        return this.state.elements.map((child, index) => {
            return (<div key={index}>{React.createElement(Elements[child.tp], child.pl)}</div>);
        });
    };
}