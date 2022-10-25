import React from "react";
import AbstractBaseSelector from "../../../gen/displays/components/AbstractBaseSelector";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

export default class BaseSelector extends AbstractBaseSelector {

	constructor(props) {
		super(props);
		this.selectorRef = React.createRef();
		this.state = {
            ...this.state,
            readonly: this.props.readonly,
    		selection: this.traceValue() ? this.traceValue() : [],
    		hiddenOptions: [],
    		options : null,
		};
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

	refreshFocused = (focused) => {
	    if (this.selectorRef == null || this.selectorRef.current == null) return;
	    window.setTimeout(() => this.selectorRef.current.focus(), 100);
	};

	refreshOptions = (options) => {
	    this.setState({ options });
	};

	children = () => {
		var result = this.props.children;

		if (this.state.options != null) {
			result = [];
			const ownerId = this.props.id;
			this.state.options.forEach(option => result.push(React.createElement(DisplayFactory.get("Text"), { id: ownerId + option, mode: 'normal', name: option, value: option, color: 'black', className: 'option'})));
		}
        else if (result == null) {
			const instances = this.instances();
			result = [];
			instances.forEach(instance => result.push(React.createElement(DisplayFactory.get(instance.tp), instance.pl)));
		}

		return result;
	};

	updateSelection = (name) => {
	    const result = [];
	    let found = false;
	    for (let i=0; i<this.state.selection.length; i++) {
	        if (this.state.selection[i] === name) found = true;
	        else result.push(this.state.selection[i]);
	    }
        if (!found && name != null) result.push(name);
	    return result;
	};

	isInSelection = (name) => {
	    const result = [];
	    for (let i=0; i<this.state.selection.length; i++)
	        if (this.state.selection[i] === name) return true;
	    return false;
    };

	isHidden = (id) => {
	    const result = [];
	    for (let i=0; i<this.state.hiddenOptions.length; i++)
	        if (id.indexOf(this.state.hiddenOptions[i]) != -1) return true;
	    return false;
    };

}