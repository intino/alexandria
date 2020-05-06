import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { List, ListItem } from '@material-ui/core';
import AbstractSelectorListBox from "../../../gen/displays/components/AbstractSelectorListBox";
import SelectorListBoxNotifier from "../../../gen/displays/notifiers/SelectorListBoxNotifier";
import SelectorListBoxRequester from "../../../gen/displays/requesters/SelectorListBoxRequester";
import Divider from './Divider';
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class SelectorListBox extends AbstractSelectorListBox {
	state = {
		selection: []
	};

	constructor(props) {
		super(props);
		this.notifier = new SelectorListBoxNotifier(this);
		this.requester = new SelectorListBoxRequester(this);
	};

	render() {
		const children = this.children();
		if (children.length <= 0) return (<div></div>);
		return (
			<List>
				{React.Children.map(children, (child, i) => { return this.renderChild(child, i); })}
			</List>
		);
	};

	renderChild = (child, key) => {
		const className = child.props.className;
		if (className != null && className.indexOf("divider") !== -1) return (<Divider/>);
		const selected = this.isInSelection(child.props.name);
		const style = selected ? {background:"#ddd"} : {};
		return (<ListItem key={key} style={style} button onClick={this.handleSelect.bind(this, child.props.name)}>{child}</ListItem>);
	};

	handleSelect = (name) => {
		const multi = this.props.multipleSelection;
	    const selection = multi ? this.updateSelection(name) : [ name ];
		this.requester.updateSelection(selection);
		this.setState({ selection: selection });
	};

	refreshSelection = (value) => {
		this.setState({ selection: value });
	};

	updateSelection = (name) => {
	    const result = [];
	    let found = false;
	    for (let i=0; i<this.state.selection.length; i++) {
	        if (this.state.selection[i] === name) found = true;
	        else result.push(this.state.selection[i]);
	    }
        if (!found) result.push(name);
	    return result;
	};

	isInSelection = (name) => {
	    const result = [];
	    for (let i=0; i<this.state.selection.length; i++)
	        if (this.state.selection[i] === name) return true;
	    return false;
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorListBox));
DisplayFactory.register("SelectorListBox", withStyles(styles, { withTheme: true })(withSnackbar(SelectorListBox)));