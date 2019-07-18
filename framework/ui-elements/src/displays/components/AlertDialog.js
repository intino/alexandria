import React from "react";
import { withStyles } from '@material-ui/core/styles';
import {Dialog, DialogContent, DialogContentText, DialogActions, Button, Dialog as MuiDialog} from "@material-ui/core";
import AbstractAlertDialog from "../../../gen/displays/components/AbstractAlertDialog";
import AlertDialogNotifier from "../../../gen/displays/notifiers/AlertDialogNotifier";
import AlertDialogRequester from "../../../gen/displays/requesters/AlertDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import BaseDialog from './BaseDialog';

const styles = theme => ({...BaseDialog.Styles(theme)});

class AlertDialog extends AbstractAlertDialog {

	constructor(props) {
		super(props);
		this.notifier = new AlertDialogNotifier(this);
		this.requester = new AlertDialogRequester(this);
	};

	render() {
		return (
			<Dialog fullScreen={this.props.fullscreen} open={this.state.opened} onClose={this.handleClose.bind(this)} TransitionComponent={this.props.fullscreen ? BaseDialog.Transition : undefined}>
				{this.renderTitle()}
				{this.renderContent(<DialogContentText>{this.props.message}</DialogContentText>)}
				<DialogActions>
					<Button onClick={this.handleClose.bind(this)} color="primary">{this.closeLabel()}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	closeLabel = () => {
		return this.translate(this.props.closeLabel != null ? this.props.closeLabel : "Close");
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(AlertDialog));
DisplayFactory.register("AlertDialog", withStyles(styles, { withTheme: true })(withSnackbar(AlertDialog)));