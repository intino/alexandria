import React from "react";
import {Divider as MuiDivider, withStyles} from '@material-ui/core';
import AbstractDivider from "../../../gen/displays/components/AbstractDivider";
import DividerNotifier from "../../../gen/displays/notifiers/DividerNotifier";
import DividerRequester from "../../../gen/displays/requesters/DividerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "notistack";

const styles = theme => ({
	divider : {
		listStyle: 'none',
		width: 'calc(100%)',
		margin: '5px 0',
	},
});

class Divider extends AbstractDivider {

	constructor(props) {
		super(props);
		this.notifier = new DividerNotifier(this);
		this.requester = new DividerRequester(this);
	};

	render() {
		return (<MuiDivider style={this.style()} className={this.props.classes.divider} component="li"/>);
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Divider));
DisplayFactory.register("Divider", withStyles(styles, { withTheme: true })(withSnackbar(Divider)));