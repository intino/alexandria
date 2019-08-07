import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { List, ListItem } from '@material-ui/core';
import AbstractSelectorListBox from "../../../gen/displays/components/AbstractSelectorListBox";
import SelectorListBoxNotifier from "../../../gen/displays/notifiers/SelectorListBoxNotifier";
import SelectorListBoxRequester from "../../../gen/displays/requesters/SelectorListBoxRequester";
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
		return (
			<List>
				{React.Children.map(children, (child, i) => { return this.renderChild(child, i); })}
			</List>
		);
	};

	renderChild = (child, key) => {
		return (
			<ListItem key={key} button onClick={this.handleSelect.bind(this, child.props.name)}>{child}</ListItem>
		);
	};

	handleSelect = (name) => {
		this.requester.updateSelection([ name ]);
		this.setState({ selection: [ name ] });
	};

	refreshSelection = (value) => {
		this.setState({ selection: value });
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorListBox));
DisplayFactory.register("SelectorListBox", withStyles(styles, { withTheme: true })(withSnackbar(SelectorListBox)));