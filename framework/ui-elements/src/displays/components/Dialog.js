import React from "react";
import {Dialog as MuiDialog, DialogContent, Typography} from "@material-ui/core"
import { withStyles } from '@material-ui/core/styles';
import AbstractDialog from "../../../gen/displays/components/AbstractDialog";
import DialogNotifier from "../../../gen/displays/notifiers/DialogNotifier";
import DialogRequester from "../../../gen/displays/requesters/DialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import BaseDialog from "./BaseDialog";

const styles = theme => ({
	...BaseDialog.Styles(theme),
	sized : {
		minHeight: "100%",
		minWidth: "100%",
	},
});

class Dialog extends AbstractDialog {

	constructor(props) {
		super(props);
		this.notifier = new DialogNotifier(this);
		this.requester = new DialogRequester(this);
	};

	render() {
		return (
			<MuiDialog fullScreen={this.props.fullscreen} open={this.state.opened}
					   onClose={this.handleClose.bind(this)}
					   TransitionComponent={this.props.fullscreen ? BaseDialog.Transition : undefined}>
				{this.renderTitle()}
				{this.renderContent(() => this.props.children)}
			</MuiDialog>
		);
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Dialog));
DisplayFactory.register("Dialog", withStyles(styles, { withTheme: true })(withSnackbar(Dialog)));