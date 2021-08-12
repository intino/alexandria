import React from "react";
import AbstractBaseSelector from "../../../gen/displays/components/AbstractBaseSelector";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

export default class BaseSelector extends AbstractBaseSelector {

	constructor(props) {
		super(props);
		this.state = {
            ...this.state,
            readonly: this.props.readonly,
    		selection: this.traceValue() ? this.traceValue() : [],
    		hiddenOptions: [],
		};
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

	children = () => {
		var result = this.props.children;

		if (result == null) {
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