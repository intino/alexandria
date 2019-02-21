import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSelectorMenu from "../../../gen/displays/components/AbstractSelectorMenu";
import SelectorMenuNotifier from "../../../gen/displays/notifiers/SelectorMenuNotifier";
import SelectorMenuRequester from "../../../gen/displays/requesters/SelectorMenuRequester";

const styles = theme => ({});

class SelectorMenu extends AbstractSelectorMenu {

	constructor(props) {
		super(props);
		this.notifier = new SelectorMenuNotifier(this);
		this.requester = new SelectorMenuRequester(this);
	};

	render() {
        const { classes } = this.props;
		return (
			<React.Fragment>{this.props.children}</React.Fragment>
		);
	};

	refreshSelected = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(SelectorMenu);